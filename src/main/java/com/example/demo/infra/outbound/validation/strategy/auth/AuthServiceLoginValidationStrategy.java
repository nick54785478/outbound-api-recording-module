package com.example.demo.infra.outbound.validation.strategy.auth;

import org.springframework.stereotype.Component;

import com.example.demo.application.shared.outbound.auth.dto.JwTokenGettenData;
import com.example.demo.infra.context.element.OutboundApiRequestInfo;
import com.example.demo.infra.outbound.shared.exception.CustomFeignException;
import com.example.demo.infra.outbound.validation.strategy.base.ApiResponseValidationStrategy;

import lombok.extern.slf4j.Slf4j;

/**
 * AuthService「登入 API」回應驗證策略。
 *
 * <p>
 * 此策略專責處理 AuthService {@code /api/v1/login} API 的回傳資料驗證， 用於判斷「業務層級」是否成功，而非 HTTP
 * 層級。
 * </p>
 *
 * <p>
 * 驗證重點：
 * <ul>
 * <li>JWT Token 是否成功取得</li>
 * <li>Refresh Token 是否存在</li>
 * </ul>
 *
 * <p>
 * 若驗證失敗，將直接拋出 {@link CustomFeignException}， 交由上層 AOP 或全域例外處理機制統一處理。
 * </p>
 *
 * <p>
 * 注意：
 * <ul>
 * <li>此策略僅關心單一 API 的業務規則</li>
 * <li>不負責策略選擇或流程控制</li>
 * </ul>
 * </p>
 */
@Slf4j
@Component
public class AuthServiceLoginValidationStrategy implements ApiResponseValidationStrategy {

	/**
	 * 所屬外部系統識別碼。
	 *
	 * @return 外部系統名稱（AuthService）
	 */
	@Override
	public String system() {
		return "AuthService";
	}

	/**
	 * 所對應的 API 路徑。
	 *
	 * <p>
	 * 此值用於策略工廠依據 system + api 動態選擇對應的驗證策略。
	 * </p>
	 *
	 * @return API 路徑（/api/v1/login）
	 */
	@Override
	public String api() {
		return "/api/v1/login";
	}

	/**
	 * 驗證 AuthService 登入 API 的回應資料是否為業務成功。
	 *
	 * <p>
	 * 驗證規則：
	 * <ul>
	 * <li>回傳物件必須為 {@link JwTokenGettenData}</li>
	 * <li>token 與 refreshToken 皆不可為 {@code null}</li>
	 * </ul>
	 * </p>
	 *
	 * <p>
	 * 若驗證失敗，將拋出 {@link CustomFeignException}， 代表該次外部 API 呼叫在業務層級失敗。
	 * </p>
	 *
	 * @param response Feign 解碼後的 API 回傳物件
	 * @param context  外部 API 呼叫上下文資訊
	 * @throws CustomFeignException 當 token 取得失敗時拋出
	 */
	@Override
	public void validate(Object response, OutboundApiRequestInfo context) {

		log.info("執行 {} 驗證策略，路徑為: {}", system(), api());

		// JWT Token 或 Refresh Token 取得失敗
		if (response instanceof JwTokenGettenData tokenData
				&& (tokenData.getToken() == null || tokenData.getRefreshToken() == null)) {
			throw new CustomFeignException("FEIGN_FAILED", "AuthService token 取得失敗");
		}
	}
}
