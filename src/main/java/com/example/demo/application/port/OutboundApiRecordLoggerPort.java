package com.example.demo.application.port;

import com.example.demo.application.shared.command.RecordOutboundApiRequestCommand;

/**
 * 負責處理 Outbound API 紀錄的 Port
 */
public interface OutboundApiRecordLoggerPort {

	/**
	 * 儲存一筆 API 請求紀錄
	 * @param command {@link RecordOutboundApiRequestCommand}
	 * @return 紀錄的 ID
	 */
	Long save(RecordOutboundApiRequestCommand command);
}
