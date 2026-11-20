package com.example.demo.application.shared.event;

/**
 * 外部 API 呼叫失敗事件
 *
 * <p>符合命名規範 3-3: N + Ved + Event</p>
 */
public class OutboundApiFailedEvent extends BaseEvent {

	/**
	 * 系統名稱
	 */
	private String system;

	/**
	 * Event Data
	 */
	private OutboundApiFailedEventData data;

	public OutboundApiFailedEvent() {
		super();
	}

	public OutboundApiFailedEvent(String eventLogUuid, String targetId, String system, OutboundApiFailedEventData data) {
		super(eventLogUuid, targetId);
		this.system = system;
		this.data = data;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public OutboundApiFailedEventData getData() {
		return data;
	}

	public void setData(OutboundApiFailedEventData data) {
		this.data = data;
	}

	public static class OutboundApiFailedEventData {
		/**
		 * 對應的 Outbound API 呼叫紀錄 ID。
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

		/**
		 * API 呼叫耗時 (毫秒)
		 */
		private Long durationMs;

		public OutboundApiFailedEventData() {
		}

		public OutboundApiFailedEventData(Long savedId, String apiPath, String errorMessage, String responseBody, String httpMethod) {
			this(savedId, apiPath, errorMessage, responseBody, httpMethod, null);
		}

		public OutboundApiFailedEventData(Long savedId, String apiPath, String errorMessage, String responseBody, String httpMethod, Long durationMs) {
			this.savedId = savedId;
			this.apiPath = apiPath;
			this.errorMessage = errorMessage;
			this.responseBody = responseBody;
			this.httpMethod = httpMethod;
			this.durationMs = durationMs;
		}

		public Long getSavedId() {
			return savedId;
		}

		public void setSavedId(Long savedId) {
			this.savedId = savedId;
		}

		public String getApiPath() {
			return apiPath;
		}

		public void setApiPath(String apiPath) {
			this.apiPath = apiPath;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		public String getResponseBody() {
			return responseBody;
		}

		public void setResponseBody(String responseBody) {
			this.responseBody = responseBody;
		}

		public String getHttpMethod() {
			return httpMethod;
		}

		public void setHttpMethod(String httpMethod) {
			this.httpMethod = httpMethod;
		}

		public Long getDurationMs() {
			return durationMs;
		}

		public void setDurationMs(Long durationMs) {
			this.durationMs = durationMs;
		}
	}
}
