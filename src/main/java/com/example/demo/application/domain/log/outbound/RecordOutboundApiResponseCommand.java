package com.example.demo.application.domain.log.outbound;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordOutboundApiResponseCommand {

	/**
	 * 回應內容 Response 物件 JSON
	 */
	private String responseBody;

	/**
	 * 錯誤訊息（若失敗則填寫）
	 */
	private String errorMessage;

	/**
	 * 狀態：SUCCESS / FAILED
	 */
	private String status;
}
