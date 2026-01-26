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
public class RecordOutboundApiSucceededEvent extends BaseEvent {

	/**
	 * 系統名稱
	 */
	private String system;

	/**
	 * Event Data
	 */
	private RecordOutboundApiEventData data;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
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
	}
}
