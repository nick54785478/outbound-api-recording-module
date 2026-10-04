package com.example.demo.application.port;

import com.example.demo.application.shared.outbound.auth.command.GetJwTokenCommand;
import com.example.demo.application.shared.outbound.auth.dto.JwTokenGottenData;
import com.example.demo.application.shared.outbound.auth.dto.PermissionGottenData;

/**
 * AuthService Client Port
 */
public interface AuthSerivceClientPort {

	/**
	 * 登入功能
	 * 
	 * @param command GetJwTokenCommand
	 * @return JwTokenGettenData
	 */
	public JwTokenGottenData getJwToken(GetJwTokenCommand command);

	/**
	 * 取得個人權限
	 * 
	 * @param username 使用者帳號
	 */
	public PermissionGottenData getPermissionList(String username);
}
