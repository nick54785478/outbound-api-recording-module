package com.example.demo.application.domain.log.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 外部 API 呼叫成功結果的封裝 Command。
 *
 * <p>
 * 此物件代表一次外部 API 呼叫成功完成後的結果快照， 提供 Response Handler 進行紀錄更新與後續處理。
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordSuccessOutboundApiCommand {

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
}
