package com.example.demo.infra.outbound.feign.decoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.example.demo.application.factory.OutboundApiResponseValidatorFactory;
import com.example.demo.application.shared.outbound.context.ContextHolder;
import com.example.demo.application.shared.outbound.context.OutboundApiRequestInfo;
import com.example.demo.infra.outbound.shared.exception.CustomFeignException;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * OpenFeign 錯誤解碼器 (Feign Error Decoder)
 *
 * <p>
 * <b>架構定位與職責：</b><br>
 * 本類別位於 <b>Infrastructure Layer (基礎設施層)</b>，實作 OpenFeign 的 {@link ErrorDecoder} 介面。<br>
 * 作為外部 API 通訊的防禦邊界（Anti-Corruption），專責攔截並解析外部服務回傳的非預期 HTTP 狀態碼或錯誤內容，
 * 將技術底層的 HTTP/Feign 例外轉換為領域或應用層可辨識的 {@link CustomFeignException}。
 * </p>
 *
 * <p>
 * <b>核心處理流程：</b>
 * <ol>
 *   <li>自 {@link ContextHolder} 取得當前請求的上下文資訊（如系統別、URL、方法）。</li>
 *   <li>解析 HTTP Response Body 內容以供日誌紀錄或錯誤診斷。</li>
 *   <li>當 HTTP 狀態碼非 2xx（如 4xx 客戶端錯誤、5xx 伺服器錯誤）時，直接封裝並拋出 {@link CustomFeignException}。</li>
 *   <li>透過 {@link OutboundApiResponseValidatorFactory} 取得該系統的專屬驗證策略，執行深入的業務邏輯檢核。</li>
 * </ol>
 * </p>
 */
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    /**
     * Outbound API 回應驗證器工廠，用於取得各外部系統專屬的驗證策略
     */
    private final OutboundApiResponseValidatorFactory validatorFactory;

    /**
     * 建構子：依賴注入回應驗證器工廠
     *
     * @param validatorFactory {@link OutboundApiResponseValidatorFactory} 驗證器工廠
     */
    public FeignErrorDecoder(OutboundApiResponseValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
    }

    /**
     * 解碼外部 API 呼叫所產生的錯誤回應
     *
     * <p>
     * 攔截 Feign 呼叫時所收到的 HTTP Response，解析 Response Body 並根據狀態碼與驗證策略轉換為對應例外。
     * </p>
     *
     * @param methodKey Feign Client 呼叫的方法識別鍵 (例如: {@code AuthFeignClient#getJwToken(GetJwTokenFromAuthCommand)})
     * @param response  Feign 原始 HTTP 回應物件 {@link Response}
     * @return 轉換後的例外物件 {@link Exception}，若無需拋出異常則回傳 {@code null}
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        // 從 ThreadLocal 取得當前請求的 Outbound 呼叫上下文
        OutboundApiRequestInfo context = ContextHolder.getFeignContext();

        // 讀取並轉換 Response Body 字串
        String body = null;
        try {
            if (response.body() != null) {
                body = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            log.warn("Feign response body 讀取失敗: methodKey={}, status={}", methodKey, response.status(), e);
        }

        // 1. 若 HTTP 狀態碼非 2xx (例如 4xx, 5xx)，封裝為專案自定義的 CustomFeignException 向上拋出
        if (response.status() < 200 || response.status() >= 300) {
            return new CustomFeignException(
                "HTTP_" + response.status(),
                "HTTP Error: " + response.status() + ", body=" + body
            );
        }

        // 2. 若 HTTP 狀態碼為 2xx，進一步委派給對應系統的 Validator 執行業務層級驗證 (例如業務錯誤碼檢查)
        try {
            if (context != null && context.system() != null) {
                validatorFactory.get(context.system()).validate(body, context);
            }
        } catch (RuntimeException e) {
            log.error("外部 API 回應業務檢核失敗: system={}, error={}", context != null ? context.system() : "UNKNOWN", e.getMessage());
            return e;
        }

        // 3. 回應成功且未違反業務檢核規則時回傳 null，Feign 將正常解碼 Response Body
        return null;
    }
}
