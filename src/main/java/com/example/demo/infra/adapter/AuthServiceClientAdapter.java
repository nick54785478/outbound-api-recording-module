package com.example.demo.infra.adapter;

import org.springframework.stereotype.Component;

import com.example.demo.application.port.out.AuthServiceClientPort;
import com.example.demo.application.shared.dto.JwTokenGottenResult;
import com.example.demo.application.shared.dto.PermissionGottenResult;
import com.example.demo.application.shared.outbound.auth.command.GetJwTokenFromAuthCommand;
import com.example.demo.application.shared.outbound.auth.query.GetPermissionListFromAuthQuery;
import com.example.demo.infra.annotation.ExternalApiClient;
import com.example.demo.infra.outbound.feign.client.AuthFeignClient;
import com.example.demo.infra.outbound.shared.dto.JwTokenGottenFromAuthData;
import com.example.demo.infra.outbound.shared.dto.PermissionGottenFromAuthData;

import lombok.AllArgsConstructor;

/**
 * AuthService Client Outbound Adapter
 *
 * <p>
 * 作為防腐層 (ACL)，將外部 Feign Client 回傳的資料結構轉換為應用層定義的 Result DTO。
 * </p>
 */
@Component
@AllArgsConstructor
@ExternalApiClient(system = "AuthService")
class AuthServiceClientAdapter implements AuthServiceClientPort {

	private final AuthFeignClient client;

	@Override
	public JwTokenGottenResult getJwToken(GetJwTokenFromAuthCommand command) {
		JwTokenGottenFromAuthData data = client.getJwToken(command);
		return data != null ? new JwTokenGottenResult(data.getToken()) : null;
	}

	@Override
	public PermissionGottenResult getPermissionList(GetPermissionListFromAuthQuery query) {
		PermissionGottenFromAuthData data = client.getPermissionList(query.username());
		return data != null ? new PermissionGottenResult(data.getPermissionList()) : null;
	}

}
