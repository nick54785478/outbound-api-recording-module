package com.example.demo.infra.outbound.exception.mapper;

import com.example.demo.config.context.element.OutboundApiRequestInfo;
import com.example.demo.infra.outbound.exception.factory.ExternalExceptionMapperFactory;

/**
 * 外部系統例外轉換器（External Exception Mapper）。
 *
 * <p>
 * 此介面用於定義「如何將外部系統回傳的錯誤結果， 轉換為本系統可理解與處理的 {@link RuntimeException}」。
 * </p>
 *
 * <p>
 * 設計背景：
 * <ul>
 * <li>不同外部系統的錯誤格式、HTTP 狀態碼設計不一致</li>
 * <li>部分外部系統即使發生錯誤，仍回傳 HTTP 200</li>
 * <li>系統內需要一個統一的 Exception 模型進行處理與紀錄</li>
 * </ul>
 * </p>
 *
 * <p>
 * 使用方式：
 * <ul>
 * <li>由 {@link ExternalExceptionMapperFactory} 依外部系統識別名稱選擇對應實作</li>
 * <li>通常於 Feign {@code ErrorDecoder} 或回應解析階段呼叫</li>
 * </ul>
 * </p>
 *
 * <p>
 * 擴充說明：
 * <ul>
 * <li>每個外部系統可實作一個專屬的 Mapper</li>
 * <li>若未實作，應提供一個 fallback Mapper（例如 DefaultExternalExceptionMapper）</li>
 * </ul>
 * </p>
 */
public interface ExternalExceptionMapper {

	/**
	 * 判斷此 Mapper 是否支援指定的外部系統。
	 *
	 * <p>
	 * 外部系統識別名稱通常來自 {@link OutboundApiRequestInfo#getSystem()}，
	 * 例如：AuthService、ERP、PaymentGateway 等。
	 * </p>
	 *
	 * @param system 外部系統識別名稱
	 * @return 當此 Mapper 可處理該外部系統時回傳 true
	 */
	boolean supports(String system);

	/**
	 * 將外部系統的錯誤回應轉換為系統內部的 Exception。
	 *
	 * <p>
	 * 此方法不應直接拋出外部系統的錯誤格式， 而是將其「翻譯」為系統內部統一的 Exception 型別。
	 * </p>
	 *
	 * <p>
	 * 注意事項：
	 * <ul>
	 * <li>httpStatus 不一定能代表成功或失敗（部分系統永遠回傳 200）</li>
	 * <li>responseBody 為外部系統原始回應內容（通常為 JSON）</li>
	 * <li>是否判斷為錯誤，通常由呼叫端流程決定</li>
	 * </ul>
	 * </p>
	 *
	 * @param httpStatus   HTTP 狀態碼
	 * @param responseBody 外部系統回傳的原始 Response Body
	 * @param context      對外 API 呼叫上下文資訊
	 * @return 對應的系統內 {@link RuntimeException}
	 */
	RuntimeException map(int httpStatus, String responseBody, OutboundApiRequestInfo context);
}
