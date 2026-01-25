package com.example.demo.infra.adapter;

import org.springframework.stereotype.Component;

import com.example.demo.application.port.AuthSerivceClientPort;
import com.example.demo.application.shared.outbound.auth.command.GetJwTokenCommand;
import com.example.demo.application.shared.outbound.auth.dto.JwTokenGettenData;
import com.example.demo.application.shared.outbound.auth.dto.PermissionGettenData;
import com.example.demo.config.annotation.ExternalApiClient;
import com.example.demo.infra.outbound.feign.client.AuthFeignClient;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
@ExternalApiClient(system = "AuthService")
class AuthSerivceClientAdapter implements AuthSerivceClientPort {

	private AuthFeignClient client;
	
	@Override
	public JwTokenGettenData getJwToken(GetJwTokenCommand command) {
		return client.getJwToken(command);
	}

	@Override
	public PermissionGettenData getPermissionList(String username) {
		return client.getPermissionList(username);
	}

}
