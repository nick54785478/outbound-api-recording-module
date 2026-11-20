package com.example.demo.application.shared.event;

/**
 * Event 基礎合約介面，包含通用欄位：訊息識別符、目標代碼。
 */
public interface BaseEvent {

    /**
     * 消息的唯一識別符
     *
     * @return 事件唯一識別碼
     */
    String eventLogUuid();

    /**
     * 目標代碼
     *
     * @return 目標識別碼
     */
    String targetId();
}
