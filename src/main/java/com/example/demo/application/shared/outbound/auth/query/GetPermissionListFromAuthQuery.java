package com.example.demo.application.shared.outbound.auth.query;

/**
 * 向外部 AuthService 查詢權限清單的 Outbound Query
 *
 * <p>
 * 專屬於應用層向外部基礎設施發出的 Outbound 意圖查詢。
 * 符合命名規範 3-5: V + N + Query。
 * </p>
 *
 * @param username 使用者名稱
 */
public record GetPermissionListFromAuthQuery(String username) {
}
