package com.example.demo.application.shared.outbound.auth.command;

/**
 * 向外部 AuthService 取得 JWT Token 的 Outbound Command
 *
 * <p>
 * 專屬於應用層向外部基礎設施發出的 Outbound 意圖命令。
 * 符合命名規範 3-4: V + N + Command。
 * </p>
 *
 * @param tenant 租戶標識
 * @param username 使用者名稱
 * @param password 密碼
 */
public record GetJwTokenFromAuthCommand(
		String tenant,
		String username,
		String password
) {
}
