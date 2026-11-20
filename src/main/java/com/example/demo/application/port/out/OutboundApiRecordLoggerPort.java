package com.example.demo.application.port.out;

import com.example.demo.application.shared.command.RecordOutboundApiRequestCommand;

/**
 * 負責處理 Outbound API 初始紀錄儲存的 Outbound Port
 *
 * <p>符合命名規範 3-8: out: ... + Port</p>
 */
public interface OutboundApiRecordLoggerPort {

	/**
	 * 儲存一筆 API 請求紀錄
	 *
	 * @param command {@link RecordOutboundApiRequestCommand}
	 * @return 紀錄的 ID
	 */
	Long save(RecordOutboundApiRequestCommand command);
}
