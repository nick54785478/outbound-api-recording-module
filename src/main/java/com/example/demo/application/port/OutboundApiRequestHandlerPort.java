package com.example.demo.application.port;

import org.aspectj.lang.ProceedingJoinPoint;

import com.example.demo.application.domain.log.outbound.RecordOutboundApiRequestCommand;
import com.example.demo.infra.annotation.ExternalApiClient;

/**
 * Outbound API Request Handler Port
 *
 * <p>
 * 此 Port 定義「外部 API 呼叫請求階段」的解析與轉換行為， 專責將 AOP 攔截到的 {@link ProceedingJoinPoint}，
 * 轉換為可被儲存或後續處理的 {@link RecordOutboundApiRequestCommand}。
 * </p>
 *
 * <h3>設計定位</h3>
 * <ul>
 * <li>屬於 Outbound API Recording 的請求端處理 Port</li>
 * <li>依「外部系統」維度進行策略分流</li>
 * <li>實作層可針對不同系統（如 ERP、CRM、第三方服務）提供客製解析</li>
 * </ul>
 *
 * <h3>責任邊界</h3>
 * <ul>
 * <li>只處理「Request 資訊」的解析</li>
 * <li>不負責實際 API 呼叫</li>
 * <li>不處理 Response / Exception</li>
 * </ul>
 */
public interface OutboundApiRequestHandlerPort {

	/**
	 * 回傳此 Handler 所支援的外部系統代碼。
	 *
	 * <p>
	 * 系統代碼通常對應 {@link ExternalApiClient#system()}， 用於在執行期選擇正確的 Request Handler 實作。
	 * </p>
	 *
	 * <p>
	 * 範例：
	 * <ul>
	 * <li>{@code ERP}</li>
	 * <li>{@code CRM}</li>
	 * <li>{@code PAYMENT}</li>
	 * </ul>
	 * </p>
	 *
	 * @return 外部系統代碼（唯一識別）
	 */
	String supportSystem();

	/**
	 * 解析 AOP 攔截到的方法呼叫， 並組裝 {@link RecordOutboundApiRequestCommand} 的 Request 資訊。
	 *
	 * <p>
	 * 此方法通常在實際呼叫外部 API 之前執行， 用於建立一筆「Outbound API 呼叫紀錄」的初始狀態。
	 * </p>
	 *
	 * <p>
	 * 解析內容可包含（由實作決定）：
	 * <ul>
	 * <li>API 方法名稱</li>
	 * <li>RequestBody / RequestParams / PathVariables</li>
	 * <li>API Path 與 HTTP Method（若可取得）</li>
	 * </ul>
	 * </p>
	 *
	 * @param joinPoint AOP 方法切入點，包含目標物件、方法與實際參數
	 * @return 已填充 Request 資訊的 {@link RecordOutboundApiRequestCommand}
	 */
	RecordOutboundApiRequestCommand resolveRequest(ProceedingJoinPoint joinPoint);

}
