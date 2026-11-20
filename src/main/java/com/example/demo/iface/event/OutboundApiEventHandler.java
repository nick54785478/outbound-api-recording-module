package com.example.demo.iface.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.demo.application.factory.OutboundApiResponseHandlerFactory;
import com.example.demo.application.port.out.OutboundApiResponseHandlerPort;
import com.example.demo.application.shared.command.RecordFailedOutboundApiCommand;
import com.example.demo.application.shared.command.RecordSuccessOutboundApiCommand;
import com.example.demo.application.shared.event.OutboundApiFailedEvent;
import com.example.demo.application.shared.event.OutboundApiFailedEvent.OutboundApiFailedEventData;
import com.example.demo.application.shared.event.OutboundApiSucceededEvent;
import com.example.demo.application.shared.event.OutboundApiSucceededEvent.OutboundApiSucceededEventData;

import lombok.extern.slf4j.Slf4j;

/**
 * Outbound API Domain Event Handler
 *
 * <p>
 * 此類別為 Event Driven Architecture（EDA）中的事件監聽器， 專責處理「外部 API 呼叫結果」相關的 Domain Event。
 * </p>
 */
@Slf4j
@Component
public class OutboundApiEventHandler {

	private final OutboundApiResponseHandlerFactory responseHandlerFactory;

	public OutboundApiEventHandler(OutboundApiResponseHandlerFactory responseHandlerFactory) {
		this.responseHandlerFactory = responseHandlerFactory;
	}

	/**
	 * 處理外部 API 呼叫「成功完成」事件
	 *
	 * @param event 外部 API 成功完成事件
	 */
	@Async
	@EventListener
	public void onSucceeded(OutboundApiSucceededEvent event) {
		String system = event.system();
		OutboundApiSucceededEventData data = event.data();

		RecordSuccessOutboundApiCommand command = new RecordSuccessOutboundApiCommand(
				data.savedId(),
				data.apiPath(),
				data.httpMethod(),
				data.responseBody(),
				data.durationMs()
		);

		OutboundApiResponseHandlerPort handler = responseHandlerFactory.getHandler(system);
		handler.handleSuccess(command);
	}

	/**
	 * 處理外部 API 呼叫「失敗」事件
	 *
	 * @param event 外部 API 呼叫失敗事件
	 */
	@Async
	@EventListener
	public void handleFailed(OutboundApiFailedEvent event) {
		String system = event.system();
		OutboundApiFailedEventData data = event.data();

		RecordFailedOutboundApiCommand command = new RecordFailedOutboundApiCommand(
				data.savedId(),
				data.apiPath(),
				data.errorMessage(),
				data.responseBody(),
				data.httpMethod(),
				data.durationMs()
		);

		OutboundApiResponseHandlerPort responseHandler = responseHandlerFactory.getHandler(system);
		responseHandler.handleFailure(command);
	}
}
