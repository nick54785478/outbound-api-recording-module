package com.example.demo.config.context.element;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 外部 API 呼叫請求資訊封裝物件。
 *
 * <p>
 * 用於描述單次對外系統（如 ERP）呼叫的基本請求資訊， 通常搭配 ThreadLocal 保存於請求生命週期中。
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OutboundApiRequestInfo {

	/**
	 * 系統名稱
	 */
	private String system;

	/**
	 * HTTP Method（GET / POST / PUT / PATCH / DELETE）
	 */
	private String httpMethod;

	/**
	 * 呼叫的 API URL（通常為 Feign 組合後的相對路徑）
	 */
	private String url;

	/**
	 * API 資源路徑
	 */
	private String api;
}
