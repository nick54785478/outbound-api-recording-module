package com.example.demo.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.application.factory.OutboundApiRequestHandlerFactory;
import com.example.demo.application.factory.OutboundApiResponseValidatorFactory;
import com.example.demo.application.port.out.EventPublisherPort;
import com.example.demo.application.port.out.JsonSerializerPort;
import com.example.demo.application.port.out.OutboundApiRecordLoggerPort;
import com.example.demo.application.port.out.OutboundApiRequestHandlerPort;
import com.example.demo.application.port.out.OutboundApiResponseValidatorPort;
import com.example.demo.application.shared.command.RecordOutboundApiRequestCommand;
import com.example.demo.application.shared.event.OutboundApiFailedEvent;
import com.example.demo.application.shared.event.OutboundApiSucceededEvent;
import com.example.demo.application.shared.outbound.context.ContextHolder;
import com.example.demo.application.shared.outbound.context.OutboundApiRequestInfo;

@ExtendWith(MockitoExtension.class)
@DisplayName("OutboundApiRecordApplicationService 測試")
class OutboundApiRecordApplicationServiceTest {

	@Mock
	private OutboundApiRequestHandlerFactory requestHandlerFactory;

	@Mock
	private OutboundApiRecordLoggerPort recordLogger;

	@Mock
	private OutboundApiResponseValidatorFactory validatorFactory;

	@Mock
	private EventPublisherPort eventPublisher;

	@Mock
	private JsonSerializerPort jsonSerializer;

	@Mock
	private OutboundApiRequestHandlerPort requestHandler;

	@Mock
	private OutboundApiResponseValidatorPort responseValidator;

	@InjectMocks
	private OutboundApiRecordApplicationService applicationService;

	@BeforeEach
	void setUp() {
		OutboundApiRequestInfo context = new OutboundApiRequestInfo();
		context.setUrl("https://api.example.com/test");
		context.setHttpMethod("POST");
		context.setSystem("AuthService");
		ContextHolder.setFeignContext(context);
	}

	@AfterEach
	void tearDown() {
		ContextHolder.clear();
	}

	/**
	 * Feature: Outbound API 呼叫前置處理
	 * Scenario: 攔截到外部呼叫時，成功解析參數並存入初始紀錄
	 * Given: 外部系統代碼、目標物件與方法參數
	 * When: 執行 preExecutingOutboundApi
	 * Then: 成功委派 RequestHandler 解析並透過 LoggerPort 持久化，回傳 Record ID
	 */
	@Test
	@DisplayName("前置處理：成功建立初始紀錄並回傳 ID")
	void preExecutingOutboundApi_success() throws Exception {
		// Given
		String system = "AuthService";
		Object target = new Object();
		Method method = Object.class.getMethod("toString");
		Object[] args = new Object[0];

		RecordOutboundApiRequestCommand command = new RecordOutboundApiRequestCommand(
				system, "toString", "GET", "/test", null, null, null
		);

		when(requestHandlerFactory.getHandler(system)).thenReturn(requestHandler);
		when(requestHandler.resolveRequest(target, method, args)).thenReturn(command);
		when(recordLogger.save(command)).thenReturn(999L);

		// When
		Long recordId = applicationService.preExecutingOutboundApi(system, target, method, args);

		// Then
		assertThat(recordId).isEqualTo(999L);
		verify(recordLogger).save(command);
	}

	/**
	 * Feature: Outbound API 呼叫後置處理與驗證
	 * Scenario: API 呼叫成功且驗證通過，發布成功領域事件
	 * Given: 執行結果物件、Record ID 與執行耗時
	 * When: 執行 afterExecutingOutboundApi
	 * Then: 驗證器檢查通過，並透過 EventPublisherPort 發布 OutboundApiSucceededEvent
	 */
	@Test
	@DisplayName("後置處理：驗證通過並發布成功領域事件")
	void afterExecutingOutboundApi_success() {
		// Given
		String system = "AuthService";
		Object proceed = "mockResponse";
		Long recordId = 1001L;
		Long durationMs = 120L;

		when(validatorFactory.get(system)).thenReturn(responseValidator);
		when(jsonSerializer.serialize(proceed)).thenReturn("{\"result\":\"ok\"}");

		// When
		applicationService.afterExecutingOutboundApi(system, proceed, recordId, durationMs);

		// Then
		ArgumentCaptor<OutboundApiSucceededEvent> captor = ArgumentCaptor.forClass(OutboundApiSucceededEvent.class);
		verify(eventPublisher).publish(captor.capture());

		OutboundApiSucceededEvent published = captor.getValue();
		assertThat(published.getSystem()).isEqualTo("AuthService");
		assertThat(published.getData().getSavedId()).isEqualTo(1001L);
		assertThat(published.getData().getDurationMs()).isEqualTo(120L);
		assertThat(published.getData().getResponseBody()).isEqualTo("{\"result\":\"ok\"}");
	}

	/**
	 * Feature: Outbound API 呼叫例外處理
	 * Scenario: API 呼叫拋出異常時，發布失敗領域事件
	 * Given: Record ID、錯誤訊息與執行耗時
	 * When: 執行 handleException
	 * Then: 成功透過 EventPublisherPort 發布 OutboundApiFailedEvent
	 */
	@Test
	@DisplayName("例外處理：捕捉錯誤並發布失敗領域事件")
	void handleException_success() {
		// Given
		String system = "AuthService";
		Long recordId = 1002L;
		String errorMessage = "Connection timed out";
		Long durationMs = 3000L;

		// When
		applicationService.handleException(system, recordId, errorMessage, durationMs);

		// Then
		ArgumentCaptor<OutboundApiFailedEvent> captor = ArgumentCaptor.forClass(OutboundApiFailedEvent.class);
		verify(eventPublisher).publish(captor.capture());

		OutboundApiFailedEvent published = captor.getValue();
		assertThat(published.getSystem()).isEqualTo("AuthService");
		assertThat(published.getData().getSavedId()).isEqualTo(1002L);
		assertThat(published.getData().getErrorMessage()).isEqualTo("Connection timed out");
		assertThat(published.getData().getDurationMs()).isEqualTo(3000L);
	}
}
