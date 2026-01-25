package com.example.demo.application.port;

import com.example.demo.application.domain.log.command.OutboundApiFailedCommand;
import com.example.demo.application.domain.log.command.OutboundApiSucceededCommand;

/**
 * Outbound API Response Handler Port
 *
 * <p>
 * 此 Port 定義「外部 API 呼叫完成後」的回應結果處理行為， 無論成功或失敗，皆以 Command 物件作為唯一輸入來源。
 * </p>
 *
 * <p>
 * Response Handler 的責任為：
 * <ul>
 * <li>根據呼叫結果，補齊或更新 Outbound API 呼叫紀錄</li>
 * <li>統一處理成功 / 失敗狀態的收斂邏輯</li>
 * <li>避免將 HTTP / Feign / Exception 細節滲入 Domain 層</li>
 * </ul>
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
	 * <p>
	 * 系統代碼用於在執行期選擇正確的 Response Handler 實作， 必須與 Request Handler 使用相同的 system 定義，
	 * 以確保請求與回應處理能正確對應。
	 * </p>
	 *
	 * @return 外部系統代碼（唯一識別）
	 */
	String supportSystem();

	/**
	 * 處理外部 API 呼叫「成功完成」後的回應結果。
	 *
	 * <p>
	 * 此方法接收的 {@link OutboundApiSucceededCommand} 為「已整理完成」的成功結果快照，通常包含：
	 * <ul>
	 * <li>原始呼叫紀錄的識別 ID（savedId）</li>
	 * <li>實際呼叫的 API Path 與 HTTP Method</li>
	 * <li>外部系統回傳的 Response Body</li>
	 * </ul>
	 * </p>
	 *
	 * <p>
	 * 實作層可依需求：
	 * <ul>
	 * <li>更新 Outbound API 紀錄狀態為 SUCCESS</li>
	 * <li>保存回傳內容以供追蹤或稽核</li>
	 * <li>觸發後續非同步流程（如事件、通知）</li>
	 * </ul>
	 * </p>
	 *
	 * @param command 成功回應結果的封裝 Command
	 */
	void handleSuccess(OutboundApiSucceededCommand command);

	/**
	 * 處理外部 API 呼叫「失敗完成」後的結果。
	 *
	 * <p>
	 * 此方法接收的 {@link OutboundApiFailedCommand} 為失敗結果的統一封裝，已將例外或錯誤資訊轉換為 可持久化與記錄的資料格式。
	 * </p>
	 *
	 * <p>
	 * 實作層通常負責：
	 * <ul>
	 * <li>更新 Outbound API 紀錄狀態為 FAILED</li>
	 * <li>保存錯誤訊息或外部系統回傳內容</li>
	 * <li>依系統需求進行告警或補償處理</li>
	 * </ul>
	 * </p>
	 *
	 * @param command 失敗結果的封裝 Command
	 */
	void handleFailure(OutboundApiFailedCommand command);

}
