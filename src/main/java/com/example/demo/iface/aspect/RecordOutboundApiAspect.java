package com.example.demo.iface.aspect;

import com.example.demo.application.shared.outbound.context.ContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


import com.example.demo.application.service.OutboundApiRecordApplicationService;
import com.example.demo.infra.annotation.ExternalApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class RecordOutboundApiAspect {

	private final OutboundApiRecordApplicationService applicationService;

	public RecordOutboundApiAspect(OutboundApiRecordApplicationService applicationService) {
		this.applicationService = applicationService;
	}


	@Pointcut("@within(com.example.demo.infra.annotation.ExternalApiClient)")
	public void pointCut() {
	}

	@Around("pointCut()")
	public Object recordApi(ProceedingJoinPoint joinPoint) throws Throwable {

		// 取得 @ExternalApiClient 標註的 system
		Class<?> targetClass = joinPoint.getTarget().getClass();
		ExternalApiClient externalApiClient = targetClass.getAnnotation(ExternalApiClient.class);
		String system = externalApiClient.system();

		Object[] args = joinPoint.getArgs();
		org.aspectj.lang.reflect.MethodSignature signature = (org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature();
		java.lang.reflect.Method method = signature.getMethod();
		Object target = joinPoint.getTarget();
		String methodName = method.getName();
		log.info("[RecordOutboundApiAspect] system: {}, Method: {}, Args: {}", system, methodName, args);

		// 外部 API 呼叫前處理
		Long recordId = applicationService.preExecutingOutboundApi(system, target, method, args);

		try {
			// 執行原方法
			Object proceed = joinPoint.proceed();

			// 外部 API 呼叫後處理
			applicationService.afterExecutingOutboundApi(system, proceed, recordId);
			return proceed;

		} catch (Exception e) {
			// 外部 API 呼叫例外處理
			applicationService.handleException(system, recordId, e.getMessage());
			throw e; // 可以拋出去終止流程，也可不拋

		} finally {
			ContextHolder.clear();
		}

	}
}
