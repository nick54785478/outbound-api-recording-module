package com.example.demo.infra.adapter;

import org.springframework.stereotype.Component;

import com.example.demo.application.port.AuthSerivceClientPort;
import com.example.demo.application.shared.outbound.auth.command.GetJwTokenCommand;
import com.example.demo.application.shared.outbound.auth.dto.JwTokenGottenData;
import com.example.demo.application.shared.outbound.auth.dto.PermissionGottenData;
import com.example.demo.infra.annotation.ExternalApiClient;
import com.example.demo.infra.outbound.feign.client.AuthFeignClient;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
@ExternalApiClient(system = "AuthService")
class AuthServiceClientAdapter implements AuthSerivceClientPort {

	private AuthFeignClient client;
	
	@Override
	public JwTokenGottenData getJwToken(GetJwTokenCommand command) {
		return client.getJwToken(command);
	}

	@Override
	public PermissionGottenData getPermissionList(String username) {
		return client.getPermissionList(username);
	}

}
