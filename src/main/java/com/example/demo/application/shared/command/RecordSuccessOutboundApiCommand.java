package com.example.demo.application.shared.command;

/**
 * 外部 API 呼叫成功結果的封裝 Command。
 *
 * <p>
 * 此物件代表一次外部 API 呼叫成功完成後的結果快照， 提供 Response Handler 進行紀錄更新與後續處理。
 * </p>
 */
public class RecordSuccessOutboundApiCommand {

	/**
	 * 對應的 Outbound API 呼叫紀錄 ID。
	 */
	private Long savedId;

	/**
	 * 實際呼叫的 API Path。
	 */
	private String apiPath;

	/**
	 * HTTP 呼叫方法（GET / POST / PUT / PATCH / DELETE）。
	 */
	private String httpMethod;

	/**
	 * 外部系統回傳的 Response Body。
	 */
	private String responseBody;

	public RecordSuccessOutboundApiCommand() {
	}

	public RecordSuccessOutboundApiCommand(Long savedId, String apiPath, String httpMethod, String responseBody) {
		this.savedId = savedId;
		this.apiPath = apiPath;
		this.httpMethod = httpMethod;
		this.responseBody = responseBody;
	}

	public Long getSavedId() { return savedId; }
	public void setSavedId(Long savedId) { this.savedId = savedId; }
	public String getApiPath() { return apiPath; }
	public void setApiPath(String apiPath) { this.apiPath = apiPath; }
	public String getHttpMethod() { return httpMethod; }
	public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }
	public String getResponseBody() { return responseBody; }
	public void setResponseBody(String responseBody) { this.responseBody = responseBody; }
}
