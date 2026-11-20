package com.example.demo.infra.adapter;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.demo.application.port.out.EventPublisherPort;
import com.example.demo.application.shared.event.BaseEvent;

import lombok.AllArgsConstructor;

/**
 * Domain Event 發佈 Outbound Adapter（Spring ApplicationEvent 實作）
 */
@Component
@AllArgsConstructor
class SpringEventPublisherAdapter implements EventPublisherPort {

	private final ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void publish(BaseEvent event) {
		applicationEventPublisher.publishEvent(event);
	}
}
