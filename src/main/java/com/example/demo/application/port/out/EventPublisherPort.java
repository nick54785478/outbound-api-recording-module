package com.example.demo.application.port.out;

import com.example.demo.application.shared.event.BaseEvent;

/**
 * 事件發布 Port 介面
 *
 * <p>符合命名規範 3-8: out: ... + Port</p>
 */
public interface EventPublisherPort {

	/**
	 * 發布領域事件
	 *
	 * @param event 欲發布的事件物件，必須繼承自 {@link BaseEvent}
	 */
	void publish(BaseEvent event);
}
