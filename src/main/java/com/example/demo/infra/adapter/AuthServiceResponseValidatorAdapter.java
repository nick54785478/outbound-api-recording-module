package com.example.demo.infra.adapter;

import org.springframework.stereotype.Component;

import com.example.demo.application.port.OutboundApiResponseValidatorPort;
import com.example.demo.infra.context.element.OutboundApiRequestInfo;
import com.example.demo.infra.outbound.validation.factory.ApiResponseValidationStrategyFactory;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class AuthServiceResponseValidatorAdapter implements OutboundApiResponseValidatorPort {

	private final ApiResponseValidationStrategyFactory strategyFactory;

	@Override
	public String supportSystem() {
		return "AuthService";
	}

	@Override
	public void validate(Object response, OutboundApiRequestInfo context) {

		String api = context.getApi(); // 例如: /api/v1/login 、 /api/v1/permissions

		// 執行策略
		strategyFactory.get(supportSystem(), api).ifPresent(strategy -> strategy.validate(response, context));

		// 沒有策略 = 預設放行
	}
}
