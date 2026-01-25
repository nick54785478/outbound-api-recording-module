package com.example.demo.application.port;

import com.example.demo.application.shared.outbound.auth.command.GetJwTokenCommand;
import com.example.demo.application.shared.outbound.auth.dto.JwTokenGettenData;
import com.example.demo.application.shared.outbound.auth.dto.PermissionGettenData;

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
	public JwTokenGettenData getJwToken(GetJwTokenCommand command);

	/**
	 * 取得個人權限
	 * 
	 * @param username 使用者帳號
	 */
	public PermissionGettenData getPermissionList(String username);
}
