package com.example.demo.application.shared.event;

/**
 * 外部 API 呼叫成功事件
 *
 * <p>符合命名規範 3-3: N + Ved + Event</p>
 *
 * @param eventLogUuid 訊息唯一識別符
 * @param targetId 目標識別碼
 * @param system 系統名稱
 * @param data 成功事件資料載體
 */
public record OutboundApiSucceededEvent(
		String eventLogUuid,
		String targetId,
		String system,
		OutboundApiSucceededEventData data
) implements BaseEvent {

	/**
	 * 外部 API 呼叫成功事件資料載體
	 *
	 * @param savedId 對應的 Outbound API 呼叫紀錄 ID
	 * @param apiPath 實際呼叫的 API Path
	 * @param httpMethod HTTP 呼叫方法（GET / POST / PUT / PATCH / DELETE）
	 * @param responseBody 外部系統回傳的 Response Body
	 * @param durationMs API 呼叫耗時 (毫秒)
	 */
	public record OutboundApiSucceededEventData(
			Long savedId,
			String apiPath,
			String httpMethod,
			String responseBody,
			Long durationMs
	) {
		public OutboundApiSucceededEventData(Long savedId, String apiPath, String httpMethod, String responseBody) {
			this(savedId, apiPath, httpMethod, responseBody, null);
		}
	}
}
