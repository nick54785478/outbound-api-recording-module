package com.example.demo.application.shared.command;

/**
 * 外部 API 回應資訊的封裝 Command。
 *
 * @param responseBody 回應內容 Response 物件 JSON
 * @param errorMessage 錯誤訊息（若失敗則填寫）
 * @param status 狀態：SUCCESS / FAILED
 */
public record RecordOutboundApiResponseCommand(
		String responseBody,
		String errorMessage,
		String status
) {
}
