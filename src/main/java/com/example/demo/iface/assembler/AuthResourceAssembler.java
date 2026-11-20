package com.example.demo.iface.assembler;

import org.springframework.stereotype.Component;

import com.example.demo.application.shared.dto.JwTokenGottenResult;
import com.example.demo.application.shared.dto.PermissionGottenResult;
import com.example.demo.application.shared.command.GetJwTokenCommand;
import com.example.demo.application.shared.query.GetPermissionListQuery;
import com.example.demo.iface.resource.in.GetJwTokenResource;
import com.example.demo.iface.resource.out.JwTokenGottenData;
import com.example.demo.iface.resource.out.JwTokenGottenResource;
import com.example.demo.iface.resource.out.PermissionGottenData;
import com.example.demo.iface.resource.out.PermissionGottenResource;

/**
 * Auth 表現層資源轉換器 (Assembler)
 *
 * <p>
 * 遵循 Clean Architecture 表現層規範：
 * 表現層與應用層的參數資料轉換（防腐層機制），一律由獨立於 resource/ 之外的 assembler/ 物件負責執行。
 * </p>
 */
@Component
public class AuthResourceAssembler {

	/**
	 * 將 Request Resource 轉換為 Application Command
	 *
	 * @param resource 表現層請求載體
	 * @return 應用層 Command
	 */
	public GetJwTokenCommand toCommand(GetJwTokenResource resource) {
		if (resource == null) {
			return null;
		}
		return new GetJwTokenCommand(resource.getTenant(), resource.getUsername(), resource.getPassword());
	}

	/**
	 * 將請求參數轉換為 Application Query
	 *
	 * @param username 使用者名稱
	 * @return 應用層 Query
	 */
	public GetPermissionListQuery toQuery(String username) {
		return new GetPermissionListQuery(username);
	}

	/**
	 * 將 Result DTO 封裝為 Response Resource
	 *
	 * @param result 應用層回傳結果 DTO
	 * @return 表現層回應載體
	 */
	public JwTokenGottenResource toResource(JwTokenGottenResult result) {
		if (result == null) {
			return new JwTokenGottenResource("200", "Success", null);
		}
		JwTokenGottenData data = new JwTokenGottenData(result.token());
		return new JwTokenGottenResource("200", "Success", data);
	}

	/**
	 * 將 Result DTO 封裝為 Response Resource
	 *
	 * @param result 應用層回傳結果 DTO
	 * @return 表現層回應載體
	 */
	public PermissionGottenResource toResource(PermissionGottenResult result) {
		if (result == null) {
			return new PermissionGottenResource("200", "Success", null);
		}
		PermissionGottenData data = new PermissionGottenData(result.permissionList());
		return new PermissionGottenResource("200", "Success", data);
	}
}
