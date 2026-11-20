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
		String system = event.getSystem();
		OutboundApiSucceededEventData data = event.getData();

		RecordSuccessOutboundApiCommand command = new RecordSuccessOutboundApiCommand(
				data.getSavedId(),
				data.getApiPath(),
				data.getHttpMethod(),
				data.getResponseBody(),
				data.getDurationMs()
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
		String system = event.getSystem();
		OutboundApiFailedEventData data = event.getData();

		RecordFailedOutboundApiCommand command = new RecordFailedOutboundApiCommand(
				data.getSavedId(),
				data.getApiPath(),
				data.getErrorMessage(),
				data.getResponseBody(),
				data.getHttpMethod(),
				data.getDurationMs()
		);

		OutboundApiResponseHandlerPort responseHandler = responseHandlerFactory.getHandler(system);
		responseHandler.handleFailure(command);
	}
}
