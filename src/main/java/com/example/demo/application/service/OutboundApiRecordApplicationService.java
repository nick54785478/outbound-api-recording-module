package com.example.demo.application.service;

import java.lang.reflect.Method;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.application.factory.OutboundApiRequestHandlerFactory;
import com.example.demo.application.factory.OutboundApiResponseValidatorFactory;
import com.example.demo.application.port.out.EventPublisherPort;
import com.example.demo.application.port.out.JsonSerializerPort;
import com.example.demo.application.port.out.OutboundApiRecordLoggerPort;
import com.example.demo.application.port.out.OutboundApiRequestHandlerPort;
import com.example.demo.application.port.out.OutboundApiResponseValidatorPort;
import com.example.demo.application.shared.command.RecordOutboundApiRequestCommand;
import com.example.demo.application.shared.event.OutboundApiFailedEvent;
import com.example.demo.application.shared.event.OutboundApiFailedEvent.OutboundApiFailedEventData;
import com.example.demo.application.shared.event.OutboundApiSucceededEvent;
import com.example.demo.application.shared.event.OutboundApiSucceededEvent.OutboundApiSucceededEventData;
import com.example.demo.application.shared.outbound.context.ContextHolder;
import com.example.demo.application.shared.outbound.context.OutboundApiRequestInfo;

/**
 * Outbound API 呼叫紀錄應用服務
 *
 * <p>
 * 此 Service 封裝了整個 Outbound API 呼叫流程的業務邏輯：
 * <ul>
 * <li>請求前處理：建立 API 呼叫紀錄</li>
 * <li>回應後處理：驗證 Response、發佈成功領域事件</li>
 * <li>例外處理：發佈失敗領域事件</li>
 * </ul>
 * 完全隔離基礎設施層技術細節。
 * </p>
 */
@Service
public class OutboundApiRecordApplicationService {

	private final OutboundApiRequestHandlerFactory outboundApiRequestHandlerFactory;
	private final OutboundApiRecordLoggerPort outboundApiRecordLogger;
	private final OutboundApiResponseValidatorFactory validatorFactory;
	private final EventPublisherPort eventPublisher;
	private final JsonSerializerPort jsonSerializer;

	public OutboundApiRecordApplicationService(
			OutboundApiRequestHandlerFactory outboundApiRequestHandlerFactory,
			OutboundApiRecordLoggerPort outboundApiRecordLogger,
			OutboundApiResponseValidatorFactory validatorFactory,
			EventPublisherPort eventPublisher,
			JsonSerializerPort jsonSerializer) {
		this.outboundApiRequestHandlerFactory = outboundApiRequestHandlerFactory;
		this.outboundApiRecordLogger = outboundApiRecordLogger;
		this.validatorFactory = validatorFactory;
		this.eventPublisher = eventPublisher;
		this.jsonSerializer = jsonSerializer;
	}

	/**
	 * 外部 API 呼叫前處理
	 *
	 * @param system 外部系統代碼
	 * @param target 目標物件
	 * @param method 目標方法
	 * @param args   方法參數
	 * @return 儲存後的紀錄 ID
	 */
	public Long preExecutingOutboundApi(String system, Object target, Method method, Object[] args) {
		OutboundApiRequestHandlerPort requestHandler = outboundApiRequestHandlerFactory.getHandler(system);
		RecordOutboundApiRequestCommand command = requestHandler.resolveRequest(target, method, args);
		return outboundApiRecordLogger.save(command);
	}

	/**
	 * 外部 API 呼叫後處理（EDA 版本）
	 *
	 * @param system   外部系統代碼
	 * @param proceed  原方法執行後回傳的 Response 物件
	 * @param recordId 對應的紀錄 ID
	 */
	public void afterExecutingOutboundApi(String system, Object proceed, Long recordId) {
		afterExecutingOutboundApi(system, proceed, recordId, null);
	}

	/**
	 * 外部 API 呼叫後處理（EDA 版本，支援呼叫耗時紀錄）
	 *
	 * @param system     外部系統代碼
	 * @param proceed    原方法執行後回傳的 Response 物件
	 * @param recordId   對應的紀錄 ID
	 * @param durationMs 呼叫耗時 (毫秒)
	 */
	public void afterExecutingOutboundApi(String system, Object proceed, Long recordId, Long durationMs) {
		OutboundApiRequestInfo feignContext = ContextHolder.getFeignContext();

		OutboundApiResponseValidatorPort validator = validatorFactory.get(system);
		validator.validate(proceed, feignContext);

		OutboundApiSucceededEvent event = new OutboundApiSucceededEvent(
				UUID.randomUUID().toString(),
				UUID.randomUUID().toString(),
				system,
				new OutboundApiSucceededEventData(
						recordId,
						feignContext.url(),
						feignContext.httpMethod(),
						jsonSerializer.serialize(proceed),
						durationMs
				)
		);

		eventPublisher.publish(event);
	}

	/**
	 * 外部 API 呼叫例外處理（EDA 版本）
	 *
	 * @param system           外部系統代碼
	 * @param recordId         對應的紀錄 ID
	 * @param exceptionMessage 發生的例外訊息
	 */
	public void handleException(String system, Long recordId, String exceptionMessage) {
		handleException(system, recordId, exceptionMessage, null);
	}

	/**
	 * 外部 API 呼叫例外處理（EDA 版本，支援呼叫耗時紀錄）
	 *
	 * @param system           外部系統代碼
	 * @param recordId         對應的紀錄 ID
	 * @param exceptionMessage 發生的例外訊息
	 * @param durationMs       呼叫耗時 (毫秒)
	 */
	public void handleException(String system, Long recordId, String exceptionMessage, Long durationMs) {
		OutboundApiRequestInfo feignContext = ContextHolder.getFeignContext();

		OutboundApiFailedEvent event = new OutboundApiFailedEvent(
				UUID.randomUUID().toString(),
				UUID.randomUUID().toString(),
				system,
				new OutboundApiFailedEventData(
						recordId,
						feignContext.url(),
						exceptionMessage,
						null,
						feignContext.httpMethod(),
						durationMs
				)
		);

		eventPublisher.publish(event);
	}
}
