package com.example.demo.application.shared.outbound.auth.query;

/**
 * 取得使用者權限清單 Query
 *
 * <p>符合命名規範 3-5: V + N + Query</p>
 */
public class GetPermissionListQuery {

	/**
	 * 使用者帳號名稱
	 */
	private String username;

	public GetPermissionListQuery() {
	}

	public GetPermissionListQuery(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
