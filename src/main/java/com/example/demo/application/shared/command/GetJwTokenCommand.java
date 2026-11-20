package com.example.demo.application.shared.command;

/**
 * 取得 JWT Token 的 Inbound Command。
 *
 * @param tenant 租戶標識
 * @param username 使用者名稱
 * @param password 密碼
 */
public record GetJwTokenCommand(
		String tenant,
		String username,
		String password
) {
}
