package com.example.demo.application.port.out;

import com.example.demo.application.shared.command.RecordFailedOutboundApiCommand;
import com.example.demo.application.shared.command.RecordSuccessOutboundApiCommand;

/**
 * Outbound API Response Handler Port
 *
 * <p>
 * 此 Port 定義「外部 API 呼叫完成後」的回應結果處理行為， 無論成功或失敗，皆以 Command 物件作為唯一輸入來源。
 * </p>
 *
 * <h3>設計定位</h3>
 * <ul>
 * <li>以「外部系統」作為策略分流維度</li>
 * <li>與 Request Handler 成對存在，但職責明確分離</li>
 * <li>僅處理「結果資料」，不感知實際呼叫技術</li>
 * </ul>
 */
public interface OutboundApiResponseHandlerPort {

	/**
	 * 回傳此 Handler 所支援的外部系統代碼。
	 *
	 * @return 外部系統代碼（唯一識別）
	 */
	String supportSystem();

	/**
	 * 處理外部 API 呼叫「成功完成」後的回應結果。
	 *
	 * @param command 成功回應結果的封裝 Command
	 */
	void handleSuccess(RecordSuccessOutboundApiCommand command);

	/**
	 * 處理外部 API 呼叫「失敗完成」後的結果。
	 *
	 * @param command 失敗結果的封裝 Command
	 */
	void handleFailure(RecordFailedOutboundApiCommand command);

}
