package com.example.demo.infra.outbound.shared.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 外部 AuthService 權限清單回應資料結構 (Feign Response DTO)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionGottenFromAuthData {

	private List<String> permissionList;

}
