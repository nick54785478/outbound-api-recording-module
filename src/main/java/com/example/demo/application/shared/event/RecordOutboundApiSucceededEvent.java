package com.example.demo.application.shared.event;

public class RecordOutboundApiSucceededEvent extends BaseEvent {

	/**
	 * 系統名稱
	 */
	private String system;

	/**
	 * Event Data
	 */
	private RecordOutboundApiEventData data;

	public RecordOutboundApiSucceededEvent() {
		super();
	}

	public RecordOutboundApiSucceededEvent(String eventLogUuid, String targetId, String system, RecordOutboundApiEventData data) {
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

	public RecordOutboundApiEventData getData() {
		return data;
	}

	public void setData(RecordOutboundApiEventData data) {
		this.data = data;
	}

	public static class RecordOutboundApiEventData {
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

		public RecordOutboundApiEventData() {
		}

		public RecordOutboundApiEventData(Long savedId, String apiPath, String httpMethod, String responseBody) {
			this.savedId = savedId;
			this.apiPath = apiPath;
			this.httpMethod = httpMethod;
			this.responseBody = responseBody;
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

		public String getHttpMethod() {
			return httpMethod;
		}

		public void setHttpMethod(String httpMethod) {
			this.httpMethod = httpMethod;
		}

		public String getResponseBody() {
			return responseBody;
		}

		public void setResponseBody(String responseBody) {
			this.responseBody = responseBody;
		}
	}
}
