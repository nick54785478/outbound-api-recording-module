package com.example.demo.application.port;

import com.example.demo.infra.context.element.OutboundApiRequestInfo;

/**
 * 外部 API 回應檢核器 Port
 *
 * <p>
 * 定義外部系統回應的檢核行為，成功則放行，失敗則拋出 Exception。
 * </p>
 */
public interface OutboundApiResponseValidatorPort {

	/**
	 * 回傳此 Validator 所支援的外部系統代碼。
	 *
	 * <p>
	 * Factory 依此在執行期選擇正確的 Validator。
	 * </p>
	 *
	 * @return 外部系統代碼（唯一識別）
	 */
	String supportSystem();

	/**
	 * 驗證外部 API 回應。
	 *
	 * @param response 回傳物件
	 * @param context  API 呼叫上下文
	 * @throws RuntimeException 當回應代表失敗時拋出
	 */
	void validate(Object response, OutboundApiRequestInfo context);
}
