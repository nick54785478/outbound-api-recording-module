package com.example.demo.application.shared.event;

/**
 * 外部 API 呼叫失敗事件
 *
 * <p>符合命名規範 3-3: N + Ved + Event</p>
 *
 * @param eventLogUuid 訊息唯一識別符
 * @param targetId 目標識別碼
 * @param system 系統名稱
 * @param data 失敗事件資料載體
 */
public record OutboundApiFailedEvent(
		String eventLogUuid,
		String targetId,
		String system,
		OutboundApiFailedEventData data
) implements BaseEvent {

	/**
	 * 外部 API 呼叫失敗事件資料載體
	 *
	 * @param savedId 對應的 Outbound API 呼叫紀錄 ID
	 * @param apiPath 實際呼叫的 API Path
	 * @param errorMessage 外部 API 回傳或系統整理後的錯誤訊息
	 * @param responseBody 外部系統回傳的 Response Body（若有）
	 * @param httpMethod HTTP 呼叫方法（GET / POST / PUT / PATCH / DELETE）
	 * @param durationMs API 呼叫耗時 (毫秒)
	 */
	public record OutboundApiFailedEventData(
			Long savedId,
			String apiPath,
			String errorMessage,
			String responseBody,
			String httpMethod,
			Long durationMs
	) {
		public OutboundApiFailedEventData(Long savedId, String apiPath, String errorMessage, String responseBody, String httpMethod) {
			this(savedId, apiPath, errorMessage, responseBody, httpMethod, null);
		}
	}
}
