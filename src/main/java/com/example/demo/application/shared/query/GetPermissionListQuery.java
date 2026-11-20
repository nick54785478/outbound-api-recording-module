package com.example.demo.application.shared.query;

/**
 * 取得使用者權限清單 Query
 *
 * <p>符合命名規範 3-5: V + N + Query</p>
 *
 * @param username 使用者帳號名稱
 */
public record GetPermissionListQuery(String username) {
}
