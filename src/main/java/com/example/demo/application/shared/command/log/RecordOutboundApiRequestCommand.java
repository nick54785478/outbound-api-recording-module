package com.example.demo.application.shared.command.log;

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

	public RecordOutboundApiRequestCommand() {
	}

	public RecordOutboundApiRequestCommand(String system, String method, String httpMethod, String apiPath, String requestBody, String requestParams, String pathVariables) {
		this.system = system;
		this.method = method;
		this.httpMethod = httpMethod;
		this.apiPath = apiPath;
		this.requestBody = requestBody;
		this.requestParams = requestParams;
		this.pathVariables = pathVariables;
	}

	public String getSystem() { return system; }
	public void setSystem(String system) { this.system = system; }
	public String getMethod() { return method; }
	public void setMethod(String method) { this.method = method; }
	public String getHttpMethod() { return httpMethod; }
	public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }
	public String getApiPath() { return apiPath; }
	public void setApiPath(String apiPath) { this.apiPath = apiPath; }
	public String getRequestBody() { return requestBody; }
	public void setRequestBody(String requestBody) { this.requestBody = requestBody; }
	public String getRequestParams() { return requestParams; }
	public void setRequestParams(String requestParams) { this.requestParams = requestParams; }
	public String getPathVariables() { return pathVariables; }
	public void setPathVariables(String pathVariables) { this.pathVariables = pathVariables; }
}
