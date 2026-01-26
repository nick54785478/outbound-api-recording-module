package com.example.demo.infra.adapter;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.demo.application.port.EventPublisherPort;
import com.example.demo.application.shared.event.BaseEvent;

import lombok.AllArgsConstructor;

/**
 * Domain Event 發佈 Adapter（Spring ApplicationEvent 實作）
 *
 * <p>
 * 此類別為 {@link EventPublisherPort} 的基礎設施層實作， 將 Domain Event 轉交給 Spring
 * {@link ApplicationEventPublisher} 發佈。
 * </p>
 *
 * <p>
 * 角色定位：
 * <ul>
 * <li>Infrastructure / Adapter Layer</li>
 * <li>負責銜接 Application Layer 與 Spring Event 機制</li>
 * </ul>
 * </p>
 *
 * <p>
 * 好處：
 * <ul>
 * <li>Application Service 不感知 Spring Event</li>
 * <li>未來可無痛替換為 Kafka / MQ / Outbox Event Publisher</li>
 * </ul>
 * </p>
 */
@Component
@AllArgsConstructor
class SpringEventPublisherAdapter implements EventPublisherPort {

	/**
	 * Spring 事件發佈器
	 */
	private final ApplicationEventPublisher applicationEventPublisher;

	/**
	 * 發佈 Domain Event（委派給 Spring ApplicationEventPublisher）
	 *
	 * @param event 要發佈的 Domain Event
	 */
	@Override
	public void publish(BaseEvent event) {
		applicationEventPublisher.publishEvent(event);
	}
}
