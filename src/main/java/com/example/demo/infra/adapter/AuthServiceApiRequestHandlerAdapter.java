package com.example.demo.infra.adapter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import com.example.demo.application.domain.log.outbound.RecordOutboundApiRequestCommand;
import com.example.demo.application.port.OutboundApiRequestHandlerPort;
import com.example.demo.infra.outbound.resolver.OutboundApiRequestResolver;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
class AuthServiceApiRequestHandlerAdapter implements OutboundApiRequestHandlerPort {

	private OutboundApiRequestResolver resolver;

	@Override
	public String supportSystem() {
		return "AuthService";
	}

	@Override
	public RecordOutboundApiRequestCommand resolveRequest(ProceedingJoinPoint joinPoint) {
		return resolver.resolveRequest(joinPoint);
	}

}
