package com.example.demo.application.shared.command;

/**
 * 外部 API 呼叫成功結果的封裝 Command。
 *
 * <p>
 * 此物件代表一次外部 API 呼叫成功完成後的結果快照， 提供 Response Handler 進行紀錄更新與後續處理。
 * </p>
 *
 * @param savedId 對應的 Outbound API 呼叫紀錄 ID
 * @param apiPath 實際呼叫的 API Path
 * @param httpMethod HTTP 呼叫方法（GET / POST / PUT / PATCH / DELETE）
 * @param responseBody 外部系統回傳的 Response Body
 * @param durationMs API 呼叫耗時 (毫秒)
 */
public record RecordSuccessOutboundApiCommand(
		Long savedId,
		String apiPath,
		String httpMethod,
		String responseBody,
		Long durationMs
) {
	public RecordSuccessOutboundApiCommand(Long savedId, String apiPath, String httpMethod, String responseBody) {
		this(savedId, apiPath, httpMethod, responseBody, null);
	}
}
