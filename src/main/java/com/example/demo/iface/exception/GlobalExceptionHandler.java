package com.example.demo.iface.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.application.shared.exception.ExternalServiceException;
import com.example.demo.application.shared.exception.ValidationException;

import lombok.extern.slf4j.Slf4j;

/**
 * 全域例外處理器
 *
 * <p>
 * 遵循 Clean Architecture 原則：表現層僅依賴 Application 層例外抽象（如 {@link ValidationException}、{@link ExternalServiceException}），
 * 完全不直接依賴基礎設施層（Infrastructure Layer）的實作細節。
 * </p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 處理 ValidationException 例外
     *
     * @param e ValidationException
     * @return BaseExceptionResponse
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<BaseExceptionResponse> handleValidationException(ValidationException e) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseExceptionResponse(e.getCode(), e.getMessage()));
    }

    /**
     * 處理外部服務通訊或業務例外（如 OutboundClientException）
     *
     * @param e ExternalServiceException
     * @return BaseExceptionResponse
     */
    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<BaseExceptionResponse> handleExternalServiceException(ExternalServiceException e) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseExceptionResponse(e.getCode(), e.getMessage()));
    }

    record BaseExceptionResponse(String code, String message) {

    }
}
