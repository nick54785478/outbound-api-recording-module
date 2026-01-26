package com.example.demo.application.port;

import com.example.demo.application.shared.event.BaseEvent;

/**
 * Domain Event 發佈 Port
 *
 * <p>
 * 此介面定義「事件發佈」的抽象能力， Application Layer 只依賴此 Port，而不直接依賴任何事件框架實作。
 * </p>
 *
 * <p>
 * 設計目的：
 * <ul>
 * <li>隔離 Application Service 與 Spring Event 機制</li>
 * <li>避免 Domain / Application Layer 直接依賴 Infrastructure 技術</li>
 * <li>提供未來切換事件機制的彈性（Kafka / RabbitMQ / Outbox Pattern）</li>
 * </ul>
 * </p>
 *
 * <p>
 * 在 EDA 架構中，此 Port 扮演「輸出端口（Outbound Port）」角色。
 * </p>
 */
public interface EventPublisherPort {

	/**
	 * 發佈 Domain Event
	 *
	 * <p>
	 * Application Service 透過此方法發佈事件， 實際的事件傳遞方式由 Adapter 層負責實作。
	 * </p>
	 *
	 * @param event 要發佈的 Domain Event
	 */
	void publish(BaseEvent event);
}
