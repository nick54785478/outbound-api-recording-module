package com.example.demo.infra.outbound.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.application.shared.outbound.auth.command.GetJwTokenCommand;
import com.example.demo.application.shared.outbound.auth.dto.JwTokenGettenData;
import com.example.demo.application.shared.outbound.auth.dto.PermissionGettenData;
import com.example.demo.config.config.AuthFeignConfiguration;

@FeignClient(value = "AuthFeignClient", url = "${auth.service.endpoint}", configuration = AuthFeignConfiguration.class)
public interface AuthFeignClient {

	/**
	 * 登入功能
	 * 
	 * @param command GetJwTokenCommand(內含帳號密碼)
	 * @return JwToken
	 */
	@PostMapping(value = "/api/v1/login")
	public JwTokenGettenData getJwToken(@RequestBody GetJwTokenCommand command);

	/**
	 * 取得個人權限
	 * 
	 * @param username 使用者帳號
	 */
	@GetMapping(value = "/api/v1/auth/permissions")
	public PermissionGettenData getPermissionList(@RequestParam String username);

}