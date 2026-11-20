package com.example.demo.application.port.in;

import com.example.demo.application.shared.dto.PermissionGottenResult;
import com.example.demo.application.shared.outbound.auth.query.GetPermissionListQuery;

/**
 * 取得使用者權限清單 UseCase
 *
 * <p>符合命名規範 3-8: in: ... + UseCase</p>
 */
public interface GetPermissionListUseCase {

	/**
	 * 依據使用者名稱查詢權限清單
	 *
	 * @param query 包含使用者名稱的 Query
	 * @return 包含權限清單的 Result DTO
	 */
	PermissionGottenResult getPermissionList(GetPermissionListQuery query);
}
