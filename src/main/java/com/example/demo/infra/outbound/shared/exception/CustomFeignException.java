package com.example.demo.infra.outbound.shared.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定義 FeignException
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomFeignException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String code; // 錯誤碼

	private final String message; // 錯誤訊息

}
