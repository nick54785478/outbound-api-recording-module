package com.example.demo.infra.adapter;

import java.lang.reflect.Method;

import org.springframework.stereotype.Component;

import com.example.demo.application.port.out.OutboundApiRequestHandlerPort;
import com.example.demo.application.shared.command.RecordOutboundApiRequestCommand;
import com.example.demo.infra.outbound.resolver.OutboundApiRequestResolver;

import lombok.AllArgsConstructor;

/**
 * AuthService Request Handler Outbound Adapter
 */
@Component
@AllArgsConstructor
class AuthServiceApiRequestHandlerAdapter implements OutboundApiRequestHandlerPort {

	private final OutboundApiRequestResolver resolver;

	@Override
	public String supportSystem() {
		return "AuthService";
	}

	@Override
	public RecordOutboundApiRequestCommand resolveRequest(Object target, Method method, Object[] args) {
		return resolver.resolveRequest(target, method, args);
	}

}
