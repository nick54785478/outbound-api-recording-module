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
 *
 * @param savedId 對應的 Outbound API 呼叫紀錄 ID
 * @param apiPath 實際呼叫的 API Path
 * @param errorMessage 外部 API 回傳或系統整理後的錯誤訊息
 * @param responseBody 外部系統回傳的 Response Body（若有）
 * @param httpMethod HTTP 呼叫方法（GET / POST / PUT / PATCH / DELETE）
 * @param durationMs API 呼叫耗時 (毫秒)
 */
public record RecordFailedOutboundApiCommand(
		Long savedId,
		String apiPath,
		String errorMessage,
		String responseBody,
		String httpMethod,
		Long durationMs
) {
	public RecordFailedOutboundApiCommand(Long savedId, String apiPath, String errorMessage, String responseBody, String httpMethod) {
		this(savedId, apiPath, errorMessage, responseBody, httpMethod, null);
	}
}
