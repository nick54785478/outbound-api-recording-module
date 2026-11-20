package com.example.demo.application.shared.command;

/**
 * 外部 API 呼叫請求資訊的封裝 Command。
 *
 * @param system 目標系統名稱 (如 AuthService、ERP)
 * @param method Client 執行方法 (Java 方法名)
 * @param httpMethod HTTP 方法 (GET / POST / PUT / DELETE)
 * @param apiPath API 路徑
 * @param requestBody 請求內容 Request 物件 JSON
 * @param requestParams 請求參數 (Query Params 或 Map 類型)
 * @param pathVariables 路徑變數 (PathVariable)
 */
public record RecordOutboundApiRequestCommand(
		String system,
		String method,
		String httpMethod,
		String apiPath,
		String requestBody,
		String requestParams,
		String pathVariables
) {
}
