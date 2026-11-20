package com.example.demo.application.port.out;

import com.example.demo.application.shared.dto.JwTokenGottenResult;
import com.example.demo.application.shared.dto.PermissionGottenResult;
import com.example.demo.application.shared.outbound.auth.command.GetJwTokenFromAuthCommand;
import com.example.demo.application.shared.outbound.auth.query.GetPermissionListFromAuthQuery;

/**
 * AuthService Client Outbound Port
 *
 * <p>
 * 定義向外部 AuthService 發出認證與授權查詢的合約。
 * 參數完全使用專屬的 Outbound Command 與 Outbound Query，不與 Inbound UseCase 耦合。
 * 符合命名規範 3-8: out: ... + Port
 * </p>
 */
public interface AuthServiceClientPort {

	/**
	 * 向外部 AuthService 取得 JWToken
	 *
	 * @param command GetJwTokenFromAuthCommand
	 * @return JwTokenGottenResult
	 */
	JwTokenGottenResult getJwToken(GetJwTokenFromAuthCommand command);

	/**
	 * 向外部 AuthService 取得個人權限清單
	 *
	 * @param query GetPermissionListFromAuthQuery
	 * @return PermissionGottenResult
	 */
	PermissionGottenResult getPermissionList(GetPermissionListFromAuthQuery query);
}
