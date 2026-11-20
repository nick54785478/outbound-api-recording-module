package com.example.demo.infra.persistence.entity;

import com.example.demo.application.shared.command.RecordFailedOutboundApiCommand;
import com.example.demo.application.shared.command.RecordOutboundApiRequestCommand;
import com.example.demo.application.shared.command.RecordSuccessOutboundApiCommand;
import com.example.demo.infra.persistence.entity.vo.OutboundApiStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 外部 API 呼叫紀錄實體
 *
 * <p>
 * 用於儲存與追蹤所有對外部系統的 API 呼叫資訊，包含 Request、Response、耗時、錯誤訊息與狀態。
 * </p>
 */
@Getter
@Entity
@Table(
        name = "outbound_api_record",
        indexes = {
                @Index(name = "idx_outbound_system_status", columnList = "system, status"),
                @Index(name = "idx_outbound_created_at", columnList = "created_at")
        }
)
public class OutboundApiRecord {

    /**
     * 最大 Payload 儲存字元長度限制（預設 50,000 字元）
     */
    private static final int MAX_PAYLOAD_LENGTH = 50000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "system", nullable = false)
    private String system;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "method")
    private String method;

    @Column(name = "url")
    private String apiPath;

    @Column(name = "request_body", columnDefinition = "nvarchar(max)")
    private String requestBody;

    @Column(name = "response_body", columnDefinition = "nvarchar(max)")
    private String responseBody;

    @Column(name = "error_message", columnDefinition = "nvarchar(max)")
    private String errorMessage;

    @Column(name = "duration_ms")
    private Long durationMs;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OutboundApiStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    /**
     * 私有無參建構子，專供 JPA / Hibernate 反射調用
     */
    private OutboundApiRecord() {
    }

    private OutboundApiRecord(Long id, String system, String httpMethod, String method, String apiPath,
                              String requestBody, String responseBody, String errorMessage, Long durationMs,
                              OutboundApiStatus status, LocalDateTime createdAt, LocalDateTime updatedAt, Long version) {
        this.id = id;
        this.system = system;
        this.httpMethod = httpMethod;
        this.method = method;
        this.apiPath = apiPath;
        this.requestBody = requestBody;
        this.responseBody = responseBody;
        this.errorMessage = errorMessage;
        this.durationMs = durationMs;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
    }

    // ------------------- Business Domain Methods -------------------

    /**
     * 根據 Request Command 建立初始紀錄（靜態工廠方法）
     *
     * @param command 包含 Request 資訊的 Command
     * @return 初始化後的 {@link OutboundApiRecord} 實體
     */
    public static OutboundApiRecord create(RecordOutboundApiRequestCommand command) {
        OutboundApiRecord apiRecord = new OutboundApiRecord();
        apiRecord.system = command.system();
        apiRecord.method = command.method();
        apiRecord.httpMethod = command.httpMethod();
        apiRecord.apiPath = command.apiPath();
        apiRecord.requestBody = apiRecord.sanitizePayload(command.requestBody());
        apiRecord.status = OutboundApiStatus.PENDING;
        apiRecord.createdAt = LocalDateTime.now();
        apiRecord.updatedAt = LocalDateTime.now();
        return apiRecord;
    }

    /**
     * 標註外部 API 呼叫成功。
     *
     * @param command {@link RecordSuccessOutboundApiCommand}
     */
    public void markSuccess(RecordSuccessOutboundApiCommand command) {
        this.status = OutboundApiStatus.SUCCESS;
        this.responseBody = sanitizePayload(command.responseBody());
        this.apiPath = command.apiPath();
        this.httpMethod = command.httpMethod();
        this.durationMs = command.durationMs();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 標註外部 API 呼叫失敗。
     *
     * @param command {@link RecordFailedOutboundApiCommand}
     */
    public void markFailed(RecordFailedOutboundApiCommand command) {
        this.status = OutboundApiStatus.FAILED;
        this.responseBody = sanitizePayload(command.responseBody());
        this.errorMessage = sanitizePayload(command.errorMessage());
        this.apiPath = command.apiPath();
        this.httpMethod = command.httpMethod();
        this.durationMs = command.durationMs();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 大資料量安全防護：自動過濾/截斷過長 Payload
     */
    private String sanitizePayload(String payload) {
        if (payload == null) {
            return null;
        }
        if (payload.length() > MAX_PAYLOAD_LENGTH) {
            return payload.substring(0, MAX_PAYLOAD_LENGTH)
                    + "... [TRUNCATED - Payload exceeded " + MAX_PAYLOAD_LENGTH + " chars]";
        }
        return payload;
    }
}
