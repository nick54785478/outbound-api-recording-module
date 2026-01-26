package com.example.demo.application.domain.log.event;

import com.example.demo.application.shared.event.BaseEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RecordOutboundApiFailedEvent extends BaseEvent {

	/**
	 * 系統名稱
	 */
	private String system;

	/**
	 * Event Data
	 */
	private RecordOutboundApiFailedEventData data;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
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
	}
}
