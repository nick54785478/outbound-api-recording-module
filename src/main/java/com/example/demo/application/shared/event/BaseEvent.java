package com.example.demo.application.shared.event;

import jakarta.persistence.MappedSuperclass;

/**
 * Event 基礎實體類，此類包含一些通用的欄位，如: 訊息識別符、目標代碼。
 * */
@MappedSuperclass
public class BaseEvent {

    /**
     * 消息的唯一識別符
     */
    protected String eventLogUuid;

    /**
     * targetId
     */
    protected String targetId;

    public BaseEvent() {
    }

    public BaseEvent(String eventLogUuid, String targetId) {
        this.eventLogUuid = eventLogUuid;
        this.targetId = targetId;
    }

    public String getEventLogUuid() {
        return eventLogUuid;
    }

    public void setEventLogUuid(String eventLogUuid) {
        this.eventLogUuid = eventLogUuid;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}