package com.example.demo.iface.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.demo.application.domain.log.command.RecordFailedOutboundApiCommand;
import com.example.demo.application.domain.log.command.RecordSuccessOutboundApiCommand;
import com.example.demo.application.domain.log.event.RecordOutboundApiFailedEvent;
import com.example.demo.application.domain.log.event.RecordOutboundApiSucceededEvent;
import com.example.demo.application.factory.OutboundApiResponseHandlerFactory;
import com.example.demo.application.port.OutboundApiResponseHandlerPort;
import com.example.demo.util.BaseDataTransformer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Outbound API Domain Event Handler
 *
 * <p>
 * 此類別為 Event Driven Architecture（EDA）中的事件監聽器， 專責處理「外部 API 呼叫結果」相關的 Domain
 * Event。
 * </p>
 *
 * <p>
 * 本 Handler 位於基礎設施層（Infrastructure / Adapter Layer）， 負責將 Application Layer 發出的
 * Domain Event 轉換為系統內部可處理的 Command，並呼叫對應的 Response Handler。
 * </p>
 *
 * <p>
 * 設計重點：
 * <ul>
 * <li>事件處理為非同步執行，避免影響主流程效能</li>
 * <li>透過 Factory 依 system 動態選擇對應的 Handler</li>
 * <li>集中處理副作用（DB 更新、紀錄、通知等）</li>
 * </ul>
 * </p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OutboundApiEventHandler {

	/**
	 * 外部 API 回應處理器工廠， 依外部系統代碼(system)取得對應的 ResponseHandler 實作。
	 */
	private final OutboundApiResponseHandlerFactory responseHandlerFactory;

	/**
	 * 處理外部 API 呼叫「成功完成」事件
	 *
	 * <p>
	 * 當 Application Service 發送 {@link RecordOutboundApiSucceededEvent} 時，
	 * 此方法會被觸發並以非同步方式執行。
	 * </p>
	 *
	 * <p>
	 * 處理流程：
	 * <ol>
	 * <li>將 Event Data 轉換為內部 Command（防腐層處理）</li>
	 * <li>依 system 取得對應的 Response Handler</li>
	 * <li>執行成功後的實際副作用處理</li>
	 * </ol>
	 * </p>
	 *
	 * @param event 外部 API 成功完成事件
	 */
	@Async
	@EventListener
	public void onSucceeded(RecordOutboundApiSucceededEvent event) {

		String system = event.getSystem();

		// 防腐層（Anti-Corruption Layer）
		// 將 Event Data 轉為系統內部可理解的 Command
		RecordSuccessOutboundApiCommand command = BaseDataTransformer.transformData(event.getData(),
				RecordSuccessOutboundApiCommand.class);

		// 依外部系統代碼取得對應的 ResponseHandler
		OutboundApiResponseHandlerPort handler = responseHandlerFactory.getHandler(system);

		// 處理外部 API 呼叫成功後的實際副作用
		handler.handleSuccess(command);
	}

	/**
	 * 處理外部 API 呼叫「失敗」事件
	 *
	 * <p>
	 * 當外部 API 呼叫或後處理流程發生例外時， Application Service 會發送
	 * {@link RecordOutboundApiFailedEvent}， 此方法將以非同步方式處理該事件。
	 * </p>
	 *
	 * <p>
	 * 處理流程：
	 * <ol>
	 * <li>將 Event Data 轉換為失敗 Command（防腐層）</li>
	 * <li>依 system 取得對應的 Response Handler</li>
	 * <li>執行失敗後的實際處理邏輯（狀態更新、記錄、補償等）</li>
	 * </ol>
	 * </p>
	 *
	 * @param event 外部 API 呼叫失敗事件
	 */
	@Async
	@EventListener
	public void handleFailed(RecordOutboundApiFailedEvent event) {

		// 防腐層（Anti-Corruption Layer）
		RecordFailedOutboundApiCommand command = BaseDataTransformer.transformData(event.getData(),
				RecordFailedOutboundApiCommand.class);

		String system = event.getSystem();

		// 依外部系統代碼取得對應的 ResponseHandler
		OutboundApiResponseHandlerPort responseHandler = responseHandlerFactory.getHandler(system);

		// 處理外部 API 呼叫失敗後的實際副作用
		responseHandler.handleFailure(command);
	}
}
