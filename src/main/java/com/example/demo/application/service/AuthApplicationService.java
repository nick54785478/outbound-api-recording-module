package com.example.demo.application.service;

import org.springframework.stereotype.Service;

import com.example.demo.application.port.in.GetJwTokenUseCase;
import com.example.demo.application.port.in.GetPermissionListUseCase;
import com.example.demo.application.port.out.AuthServiceClientPort;
import com.example.demo.application.shared.dto.JwTokenGottenResult;
import com.example.demo.application.shared.dto.PermissionGottenResult;
import com.example.demo.application.shared.command.GetJwTokenCommand;
import com.example.demo.application.shared.outbound.auth.command.GetJwTokenFromAuthCommand;
import com.example.demo.application.shared.outbound.auth.query.GetPermissionListFromAuthQuery;
import com.example.demo.application.shared.query.GetPermissionListQuery;

import lombok.AllArgsConstructor;

/**
 * Auth 應用層服務
 *
 * <p>
 * 實作 Inbound UseCase 介面（GetJwTokenUseCase, GetPermissionListUseCase），
 * 並將 Inbound 意圖（Command/Query）轉換為具備專屬業務語意的 Outbound 意圖（GetJwTokenFromAuthCommand / GetPermissionListFromAuthQuery）
 * 傳遞給 Outbound Port (AuthServiceClientPort)。徹底打破 Command/Query 直拋的耦合問題。
 * </p>
 */
@Service
@AllArgsConstructor
public class AuthApplicationService implements GetJwTokenUseCase, GetPermissionListUseCase {

	private final AuthServiceClientPort authServiceClient;

	@Override
	public JwTokenGottenResult getJwToken(GetJwTokenCommand command) {
		// 顯式轉換為向外部 AuthService 發出認證的 Outbound Command
		GetJwTokenFromAuthCommand outboundCommand = new GetJwTokenFromAuthCommand(
				command.tenant(),
				command.username(),
				command.password()
		);
		return authServiceClient.getJwToken(outboundCommand);
	}

	@Override
	public PermissionGottenResult getPermissionList(GetPermissionListQuery query) {
		// 顯式轉換為向外部 AuthService 查詢權限的 Outbound Query
		GetPermissionListFromAuthQuery outboundQuery = new GetPermissionListFromAuthQuery(query.username());
		return authServiceClient.getPermissionList(outboundQuery);
	}
}
