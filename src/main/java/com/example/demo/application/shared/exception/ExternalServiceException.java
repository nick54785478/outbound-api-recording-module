package com.example.demo.application.shared.exception;

import lombok.Getter;

/**
 * 外部服務呼叫異常（應用層通用例外）
 *
 * <p>
 * 定義於 Application Layer，代表向外部第三方或相依微服務調用時發生的通訊或業務例外。
 * 作為所有基礎設施層對外客戶端例外（如 {@code OutboundClientException}）的抽象基底類別。
 * 依循依賴反轉原則（DIP），表現層全域例外處理器（{@code GlobalExceptionHandler}）僅依賴此應用層例外，
 * 徹底隔絕表現層（Inbound Adapter）對基礎設施層（Outbound Adapter）的直接依賴。
 * </p>
 */
@Getter
public class ExternalServiceException extends RuntimeException {

    private final String code;

    public ExternalServiceException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ExternalServiceException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
