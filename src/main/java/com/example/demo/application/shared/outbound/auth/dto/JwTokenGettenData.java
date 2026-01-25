package com.example.demo.application.shared.outbound.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwTokenGettenData {

	private String token;

	private String refreshToken;
}