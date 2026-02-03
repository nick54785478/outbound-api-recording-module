package com.example.demo.infra.outbound.validation.strategy.base;

import com.example.demo.infra.context.element.OutboundApiRequestInfo;

/**
 * 外部 API 回應驗證策略（API 級別）
 *
 * <p>
 * 每一個實作只關心「某一支 API」的成功與失敗判斷規則。
 * </p>
 */
public interface ApiResponseValidationStrategy {

	/**
	 * 所屬外部系統
	 */
	String system();

	/**
	 * 所屬 API 識別碼，對應 Feign Client 內設定的路徑
	 */
	String api();

	/**
	 * 驗證回應是否為業務成功
	 *
	 * @param response Feign 解碼後的回傳物件
	 * @param context  呼叫上下文
	 * @throws RuntimeException 若驗證失敗
	 */
	void validate(Object response, OutboundApiRequestInfo context);
}
