package com.example.demo.infra.outbound.exception.adapter;

import org.springframework.stereotype.Component;

import com.example.demo.config.context.element.OutboundApiRequestInfo;
import com.example.demo.infra.outbound.exception.factory.ExternalExceptionMapperFactory;
import com.example.demo.infra.outbound.exception.mapper.ExternalExceptionMapper;
import com.example.demo.infra.outbound.shared.exception.CustomFeignException;

/**
 * Auth Service 專用的外部例外轉換器。
 *
 * <p>
 * 負責將 Auth Service 回傳的錯誤內容，統一轉換為系統內部可識別的 Exception。
 * </p>
 *
 * <p>
 * 此 Mapper 僅在 {@link OutboundApiRequestInfo#getSystem()} 為
 * {@code "AuthService"} 時才會被 {@link ExternalExceptionMapperFactory} 選用。
 * </p>
 *
 * <p>
 * ⚠ 注意：
 * <ul>
 * <li>目前 Auth Service 的錯誤格式為 HTTP 200 + 錯誤 body</li>
 * <li>此 Mapper 不依賴 HTTP Status，而是由上層決定是否進入錯誤處理流程</li>
 * </ul>
 * </p>
 */
@Component
class AuthServiceExceptionMapper implements ExternalExceptionMapper {

	/**
	 * 判斷此 Mapper 是否支援指定的外部系統。
	 *
	 * @param system 外部系統識別名稱（由 OutboundApiRequestInfo 提供）
	 * @return 當 system 為 "AuthService" 時回傳 true
	 */
	@Override
	public boolean supports(String system) {
		return "AuthService".equals(system);
	}

	/**
	 * 將 Auth Service 的錯誤回應轉換為系統內部 Exception。
	 *
	 * @param httpStatus   HTTP 狀態碼（此系統實際上可能永遠是 200）
	 * @param responseBody 外部系統原始回應內容（JSON 字串）
	 * @param context      對外 API 呼叫的上下文資訊
	 * @return 封裝後的 {@link CustomFeignException}
	 */
	@Override
	public RuntimeException map(int httpStatus, String responseBody, OutboundApiRequestInfo context) {
		return new CustomFeignException("FEIGN_FAILED", "呼叫 Auth Service 外部 API 失敗");
	}
}
