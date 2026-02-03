package com.example.demo.infra.outbound.exception.adapter;

import org.springframework.stereotype.Component;

import com.example.demo.infra.context.element.OutboundApiRequestInfo;
import com.example.demo.infra.outbound.exception.mapper.ExternalExceptionMapper;
import com.example.demo.infra.outbound.shared.exception.CustomFeignException;

/**
 * 預設的外部系統 Exception Mapper（Fallback）。
 *
 * <p>
 * 當沒有任何特定外部系統的 Mapper 符合時， 由此 Mapper 作為最後防線，避免 Exception 無法轉換。
 * </p>
 *
 * <p>
 * 此 Mapper 的 {@link #supports(String)} 永遠回傳 true， 因此<strong>務必確保</strong>：
 * <ul>
 * <li>它的 Bean 注入順序在其他具體 Mapper 之後</li>
 * </ul>
 * </p>
 */
@Component
class DefaultExternalExceptionMapper implements ExternalExceptionMapper {

	/**
	 * 永遠支援任何系統，作為 fallback 使用。
	 *
	 * @param system 外部系統識別名稱
	 * @return 永遠為 true
	 */
	@Override
	public boolean supports(String system) {
		return true;
	}

	/**
	 * 將未知或未特別處理的外部系統錯誤， 轉換為通用的 {@link CustomFeignException}。
	 *
	 * @param httpStatus   HTTP 狀態碼
	 * @param responseBody 外部系統原始回應內容
	 * @param context      對外 API 呼叫上下文
	 * @return 通用型 Feign Exception
	 */
	@Override
	public RuntimeException map(int httpStatus, String responseBody, OutboundApiRequestInfo context) {
		return new CustomFeignException("FEIGN_FAILED", "外部系統 " + context.getSystem() + " 呼叫失敗");
	}
}
