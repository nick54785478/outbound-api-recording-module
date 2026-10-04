package com.example.demo.application.service;

import org.springframework.stereotype.Service;

import com.example.demo.application.port.AuthSerivceClientPort;
import com.example.demo.application.shared.outbound.auth.command.GetJwTokenCommand;
import com.example.demo.application.shared.outbound.auth.dto.JwTokenGottenData;
import com.example.demo.application.shared.outbound.auth.dto.PermissionGottenData;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthApplicationService {

	private AuthSerivceClientPort authServiceClient;

	/**
	 * 向 Auth Service 取得 JWToken
	 * 
	 * @param command GetJwTokenCommand
	 * @return Token 資料
	 */
	public JwTokenGottenData getJwToken(GetJwTokenCommand command) {
		return authServiceClient.getJwToken(command);
	}

	/**
	 * 取得使用者的 Permission 清單
	 * 
	 * @param username 使用者帳號
	 * @return Permission 清單
	 */
	public PermissionGottenData getPermissionList(String username) {
		return authServiceClient.getPermissionList(username);
	}
}
