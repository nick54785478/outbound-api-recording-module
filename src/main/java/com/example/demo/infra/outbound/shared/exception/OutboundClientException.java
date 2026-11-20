package com.example.demo.infra.outbound.shared.exception;

import com.example.demo.application.shared.exception.ExternalServiceException;

/**
 * 外部系統通訊異常 (Outbound Client Exception)
 *
 * <p>
 * 位於 Infrastructure Layer，實作外部 HTTP Client (OpenFeign 等) 呼叫時所產生的通訊或檢核失敗例外。
 * 繼承自 Application 層的 {@link ExternalServiceException}，遵循依賴反轉原則 (DIP)。
 * </p>
 */
public class OutboundClientException extends ExternalServiceException {

    public OutboundClientException(String code, String message) {
        super(code, message);
    }

    public OutboundClientException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
