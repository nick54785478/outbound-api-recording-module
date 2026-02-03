package com.example.demo.infra.exception.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseExceptionResponse {

	private String code;

	private String message;
}
