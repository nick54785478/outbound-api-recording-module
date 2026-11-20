package com.example.demo.infra.outbound.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 外部 AuthService 登入回應資料結構 (Feign Response DTO)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwTokenGottenFromAuthData {

	private String token;

}
