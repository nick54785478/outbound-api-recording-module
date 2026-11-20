package com.example.demo.infra.outbound.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.application.shared.outbound.auth.command.GetJwTokenFromAuthCommand;
import com.example.demo.config.AuthFeignConfiguration;
import com.example.demo.infra.outbound.shared.dto.JwTokenGottenFromAuthData;
import com.example.demo.infra.outbound.shared.dto.PermissionGottenFromAuthData;

@FeignClient(value = "AuthFeignClient", url = "${auth.service.endpoint}", configuration = AuthFeignConfiguration.class)
public interface AuthFeignClient {

	/**
	 * 登入功能
	 *
	 * @param command GetJwTokenFromAuthCommand (內含帳號密碼)
	 * @return JwToken
	 */
	@PostMapping(value = "/api/v1/login")
	public JwTokenGottenFromAuthData getJwToken(@RequestBody GetJwTokenFromAuthCommand command);

	/**
	 * 取得個人權限
	 *
	 * @param username 使用者帳號
	 */
	@GetMapping(value = "/api/v1/auth/permissions")
	public PermissionGottenFromAuthData getPermissionList(@RequestParam String username);

}
