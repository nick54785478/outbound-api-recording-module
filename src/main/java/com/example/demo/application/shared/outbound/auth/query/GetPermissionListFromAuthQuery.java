package com.example.demo.application.shared.outbound.auth.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 向外部 AuthService 查詢權限清單的 Outbound Query
 *
 * <p>
 * 專屬於應用層向外部基礎設施發出的 Outbound 意圖查詢。
 * 符合命名規範 3-5: V + N + Query。
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPermissionListFromAuthQuery {

	private String username;
}
