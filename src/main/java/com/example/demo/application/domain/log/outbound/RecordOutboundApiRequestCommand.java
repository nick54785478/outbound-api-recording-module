package com.example.demo.application.domain.log.outbound;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordOutboundApiRequestCommand {

	/**
	 * 目標系統名稱 (目前僅有 AuthPlatform、ERP)
	 */
	private String system;

	/**
	 * Client 執行方法 (Java 方法名)
	 */
	private String method;

	/**
	 * HTTP 方法 (GET / POST / PUT / DELETE)
	 */
	private String httpMethod;

	/**
	 * API 路徑
	 */
	private String apiPath;

	/**
	 * 請求內容 Request 物件 JSON
	 */
	private String requestBody;

	/**
	 * 請求參數 (Query Params 或 Map 類型)
	 */
	private String requestParams;

	/**
	 * 路徑變數 (PathVariable)
	 */
	private String pathVariables;

}
