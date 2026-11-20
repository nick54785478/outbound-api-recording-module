package com.example.demo.application.shared.event;

/**
 * 外部 API 呼叫成功事件
 *
 * <p>符合命名規範 3-3: N + Ved + Event</p>
 */
public class OutboundApiSucceededEvent extends BaseEvent {

	/**
	 * 系統名稱
	 */
	private String system;

	/**
	 * Event Data
	 */
	private OutboundApiSucceededEventData data;

	public OutboundApiSucceededEvent() {
		super();
	}

	public OutboundApiSucceededEvent(String eventLogUuid, String targetId, String system, OutboundApiSucceededEventData data) {
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

	public OutboundApiSucceededEventData getData() {
		return data;
	}

	public void setData(OutboundApiSucceededEventData data) {
		this.data = data;
	}

	public static class OutboundApiSucceededEventData {
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

		/**
		 * API 呼叫耗時 (毫秒)
		 */
		private Long durationMs;

		public OutboundApiSucceededEventData() {
		}

		public OutboundApiSucceededEventData(Long savedId, String apiPath, String httpMethod, String responseBody) {
			this(savedId, apiPath, httpMethod, responseBody, null);
		}

		public OutboundApiSucceededEventData(Long savedId, String apiPath, String httpMethod, String responseBody, Long durationMs) {
			this.savedId = savedId;
			this.apiPath = apiPath;
			this.httpMethod = httpMethod;
			this.responseBody = responseBody;
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

		public Long getDurationMs() {
			return durationMs;
		}

		public void setDurationMs(Long durationMs) {
			this.durationMs = durationMs;
		}
	}
}
