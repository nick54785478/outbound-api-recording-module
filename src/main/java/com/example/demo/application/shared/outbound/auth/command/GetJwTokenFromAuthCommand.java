package com.example.demo.application.shared.outbound.auth.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 向外部 AuthService 取得 JWT Token 的 Outbound Command
 *
 * <p>
 * 專屬於應用層向外部基礎設施發出的 Outbound 意圖命令。
 * 符合命名規範 3-4: V + N + Command。
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetJwTokenFromAuthCommand {

	private String tenant;

	private String username;

	private String password;
}
