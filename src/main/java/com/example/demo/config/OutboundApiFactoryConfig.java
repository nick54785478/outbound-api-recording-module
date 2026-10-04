package com.example.demo.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.application.factory.OutboundApiRequestHandlerFactory;
import com.example.demo.application.factory.OutboundApiResponseHandlerFactory;
import com.example.demo.application.factory.OutboundApiResponseValidatorFactory;
import com.example.demo.application.port.OutboundApiRequestHandlerPort;
import com.example.demo.application.port.OutboundApiResponseHandlerPort;
import com.example.demo.application.port.OutboundApiResponseValidatorPort;

/**
 * 負責實例化 Application Layer 的 Factory 類別。
 *
 * 將 Spring 的依賴注入 (@Bean) 推遲到 Infrastructure / Config 層，
 * 避免 Application 層的程式碼被 Spring 框架技術侵入。
 */
@Configuration
public class OutboundApiFactoryConfig {

	@Bean
	public OutboundApiRequestHandlerFactory outboundApiRequestHandlerFactory(
			List<OutboundApiRequestHandlerPort> handlers) {
		return new OutboundApiRequestHandlerFactory(handlers);
	}

	@Bean
	public OutboundApiResponseHandlerFactory outboundApiResponseHandlerFactory(
			List<OutboundApiResponseHandlerPort> handlers) {
		return new OutboundApiResponseHandlerFactory(handlers);
	}

	@Bean
	public OutboundApiResponseValidatorFactory outboundApiResponseValidatorFactory(
			List<OutboundApiResponseValidatorPort> validators) {
		return new OutboundApiResponseValidatorFactory(validators);
	}
}
