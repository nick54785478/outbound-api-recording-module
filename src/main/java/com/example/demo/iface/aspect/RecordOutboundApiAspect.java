package com.example.demo.iface.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.example.demo.application.domain.log.aggregate.OutboundApiRecord;
import com.example.demo.application.domain.log.command.OutboundApiFailedCommand;
import com.example.demo.application.domain.log.command.OutboundApiSucceededCommand;
import com.example.demo.application.domain.log.outbound.RecordOutboundApiRequestCommand;
import com.example.demo.application.factory.OutboundApiRequestHandlerFactory;
import com.example.demo.application.factory.OutboundApiResponseHandlerFactory;
import com.example.demo.application.factory.OutboundApiResponseValidatorFactory;
import com.example.demo.application.port.OutboundApiRequestHandlerPort;
import com.example.demo.application.port.OutboundApiResponseHandlerPort;
import com.example.demo.application.port.OutboundApiResponseValidatorPort;
import com.example.demo.config.annotation.ExternalApiClient;
import com.example.demo.config.context.ContextHolder;
import com.example.demo.config.context.element.OutboundApiRequestInfo;
import com.example.demo.infra.persistence.OutboundApiRecordRepository;
import com.example.demo.util.JsonParseUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RecordOutboundApiAspect {

	private final OutboundApiRequestHandlerFactory outboundApiRequestHandlerFactory;
	private final OutboundApiRecordRepository outboundApiRecordRepository;
	private final OutboundApiResponseHandlerFactory outboundApiResponseHandlerFactory;
	private final OutboundApiResponseValidatorFactory validatorFactory; // 外部 API 回應驗證器 Factory

	@Pointcut("@within(com.example.demo.config.annotation.ExternalApiClient)")
	public void pointCut() {
	}

	@Around("pointCut()")
	public Object recordApi(ProceedingJoinPoint joinPoint) throws Throwable {

		// 取得 @ExternalApiClient 標註的 system
		Class<?> targetClass = joinPoint.getTarget().getClass();
		ExternalApiClient externalApiClient = targetClass.getAnnotation(ExternalApiClient.class);
		String system = externalApiClient.system();

		Object[] args = joinPoint.getArgs();
		String methodName = joinPoint.getSignature().getName();
		log.info("[RecordOutboundApiAspect] system: {}, Method: {}, Args: {}", system, methodName, args);

		// 取得 Request Handler
		OutboundApiRequestHandlerPort requestHandler = outboundApiRequestHandlerFactory.getHandler(system);

		// 將方法參數轉換為 Request Command
		RecordOutboundApiRequestCommand command = requestHandler.resolveRequest(joinPoint);

		// 建立 OutboundApiRecord 並儲存
		OutboundApiRecord outboundApiRecord = new OutboundApiRecord();
		outboundApiRecord.create(command);
		OutboundApiRecord saved = outboundApiRecordRepository.save(outboundApiRecord);

		// 取得 Response Handler
		OutboundApiResponseHandlerPort responseHandler = outboundApiResponseHandlerFactory.getHandler(system);

		try {
			// 執行原方法
			Object proceed = joinPoint.proceed();

			OutboundApiRequestInfo feignContext = ContextHolder.getFeignContext();

			// --- 新增：呼叫 Validator ---
			OutboundApiResponseValidatorPort validator = validatorFactory.get(system);
			validator.validate(proceed, feignContext); // 這裡直接傳物件，不用 JSON

			// --- 成功處理 ---
			OutboundApiSucceededCommand outboundApiSucceededCommand = OutboundApiSucceededCommand.builder()
					.savedId(saved.getId()).apiPath(feignContext.getUrl()).httpMethod(feignContext.getHttpMethod())
					.responseBody(JsonParseUtil.serialize(proceed)).build();

			responseHandler.handleSuccess(outboundApiSucceededCommand);

			return proceed;

		} catch (Exception e) {
			OutboundApiRequestInfo feignContext = ContextHolder.getFeignContext();

			OutboundApiFailedCommand outboundApiFailedCommand = OutboundApiFailedCommand.builder()
					.savedId(saved.getId()).apiPath(feignContext.getUrl()).httpMethod(feignContext.getHttpMethod())
					.errorMessage(e.getMessage()).build();

			// 發生例外處理
			responseHandler.handleFailure(outboundApiFailedCommand);
			throw e;
		} finally {
			ContextHolder.clear();
		}
	}
}
