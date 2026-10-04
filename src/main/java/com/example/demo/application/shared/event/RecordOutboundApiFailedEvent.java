package com.example.demo.application.shared.event;

public class RecordOutboundApiFailedEvent extends BaseEvent {

	/**
	 * 系統名稱
	 */
	private String system;

	/**
	 * Event Data
	 */
	private RecordOutboundApiFailedEventData data;

	public RecordOutboundApiFailedEvent() {
		super();
	}

	public RecordOutboundApiFailedEvent(String eventLogUuid, String targetId, String system, RecordOutboundApiFailedEventData data) {
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

	public RecordOutboundApiFailedEventData getData() {
		return data;
	}

	public void setData(RecordOutboundApiFailedEventData data) {
		this.data = data;
	}

	public static class RecordOutboundApiFailedEventData {
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

		public RecordOutboundApiFailedEventData() {
		}

		public RecordOutboundApiFailedEventData(Long savedId, String apiPath, String errorMessage, String responseBody, String httpMethod) {
			this.savedId = savedId;
			this.apiPath = apiPath;
			this.errorMessage = errorMessage;
			this.responseBody = responseBody;
			this.httpMethod = httpMethod;
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
	}
}
