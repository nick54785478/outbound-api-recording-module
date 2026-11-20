package com.example.demo.application.port.out;

import java.lang.reflect.Method;

import com.example.demo.application.shared.command.RecordOutboundApiRequestCommand;

/**
 * Outbound API Request Handler Port
 *
 * <p>
 * 此 Port 定義「外部 API 呼叫請求階段」的解析與轉換行為， 專責將 AOP 攔截到的 ProceedingJoinPoint
 * 轉換為可被儲存或後續處理的 {@link RecordOutboundApiRequestCommand}。
 * </p>
 *
 * <h3>設計定位</h3>
 * <ul>
 * <li>屬於 Outbound API Recording 的請求端處理 Outbound Port</li>
 * <li>依「外部系統」維度進行策略分流</li>
 * <li>實作層可針對不同系統（如 ERP、CRM、第三方服務）提供客製解析</li>
 * </ul>
 */
public interface OutboundApiRequestHandlerPort {

	/**
	 * 回傳此 Handler 所支援的外部系統代碼。
	 *
	 * @return 外部系統代碼（唯一識別）
	 */
	String supportSystem();

	/**
	 * 解析 AOP 攔截到的方法呼叫， 並組裝 {@link RecordOutboundApiRequestCommand} 的 Request 資訊。
	 *
	 * @param target 攔截到的目標物件
	 * @param method 攔截到的目標方法
	 * @param args 方法參數
	 * @return 已填充 Request 資訊的 {@link RecordOutboundApiRequestCommand}
	 */
	RecordOutboundApiRequestCommand resolveRequest(Object target, Method method, Object[] args);

}
