package com.example.demo.iface.exception;

import org.modelmapper.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.infra.exception.response.BaseExceptionResponse;
import com.example.demo.infra.outbound.shared.exception.CustomFeignException;
import com.example.demo.util.BaseDataTransformer;

import lombok.extern.slf4j.Slf4j;

/**
 * 全域例外處理器
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
				.body(BaseDataTransformer.transformData(e, BaseExceptionResponse.class));
	}

	/**
	 * 處理 FeignException 例外
	 * 
	 * @param e CustomFeignException
	 * @return BaseExceptionResponse
	 */
	@ExceptionHandler(CustomFeignException.class)
	public ResponseEntity<BaseExceptionResponse> handleFeignException(CustomFeignException e) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(BaseDataTransformer.transformData(e, BaseExceptionResponse.class));
	}

}