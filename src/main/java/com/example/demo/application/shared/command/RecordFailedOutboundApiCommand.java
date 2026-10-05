package com.example.demo.application.shared.command;

/**
 * 外部 API 呼叫失敗結果的封裝 Command。
 *
 * <p>
 * 此物件代表一次外部 API 呼叫「已確定失敗」後的結果快照， 用於傳遞給 Response Handler 進行後續處理。
 * </p>
 *
 * <p>
 * 該 Command 不包含任何 Exception 或 HTTP Client 物件， 僅保留可記錄、可持久化的錯誤資訊。
 * </p>
 */
public class RecordFailedOutboundApiCommand {

	/**
	 * 對應的 Outbound API 呼叫紀錄 ID。
	 *
	 * <p>
	 * 用於關聯 Request 階段所建立的紀錄資料。
	 * </p>
	 */
	private Long savedId;

	/**
	 * 實際呼叫的 API Path。
	 */
	private String apiPath;

	/**
	 * 外部 API 回傳或系統整理後的錯誤訊息。
	 */
	private String errorMessage;

	/**
	 * 外部系統回傳的 Response Body（若有）。
	 */
	private String responseBody;

	/**
	 * HTTP 呼叫方法（GET / POST / PUT / PATCH / DELETE）。
	 */
	private String httpMethod;

	public RecordFailedOutboundApiCommand() {
	}

	public RecordFailedOutboundApiCommand(Long savedId, String apiPath, String errorMessage, String responseBody, String httpMethod) {
		this.savedId = savedId;
		this.apiPath = apiPath;
		this.errorMessage = errorMessage;
		this.responseBody = responseBody;
		this.httpMethod = httpMethod;
	}

	public Long getSavedId() { return savedId; }
	public void setSavedId(Long savedId) { this.savedId = savedId; }
	public String getApiPath() { return apiPath; }
	public void setApiPath(String apiPath) { this.apiPath = apiPath; }
	public String getErrorMessage() { return errorMessage; }
	public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
	public String getResponseBody() { return responseBody; }
	public void setResponseBody(String responseBody) { this.responseBody = responseBody; }
	public String getHttpMethod() { return httpMethod; }
	public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }
}
