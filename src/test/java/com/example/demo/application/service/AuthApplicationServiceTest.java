package com.example.demo.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.application.port.out.AuthServiceClientPort;
import com.example.demo.application.shared.dto.JwTokenGottenResult;
import com.example.demo.application.shared.dto.PermissionGottenResult;
import com.example.demo.application.shared.command.GetJwTokenCommand;
import com.example.demo.application.shared.outbound.auth.command.GetJwTokenFromAuthCommand;
import com.example.demo.application.shared.outbound.auth.query.GetPermissionListFromAuthQuery;
import com.example.demo.application.shared.query.GetPermissionListQuery;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthApplicationService UseCase 測試")
class AuthApplicationServiceTest {

	@Mock
	private AuthServiceClientPort authServiceClient;

	@InjectMocks
	private AuthApplicationService authApplicationService;

	/**
	 * Feature: 取得 JWT Token
	 * Scenario: 使用者提供正確帳密，成功將 Inbound Command 轉為 Outbound Command 向外部 AuthService 取得 Token
	 * Given: 包含 tenant, username, password 的登入 Command (GetJwTokenCommand)
	 * When: 呼叫 GetJwTokenUseCase.getJwToken
	 * Then: 成功轉換為 GetJwTokenFromAuthCommand 並透過 AuthServiceClientPort 回傳 JwTokenGottenResult
	 */
	@Test
	@DisplayName("成功將 Inbound Command 轉換為 Outbound Command 並取得 JWT Token")
	void getJwToken_success() {
		// Given
		GetJwTokenCommand inboundCommand = new GetJwTokenCommand("default", "admin", "pwd123");
		GetJwTokenFromAuthCommand expectedOutboundCommand = new GetJwTokenFromAuthCommand("default", "admin", "pwd123");
		JwTokenGottenResult expectedResult = new JwTokenGottenResult("mock-token-xyz");
		when(authServiceClient.getJwToken(expectedOutboundCommand)).thenReturn(expectedResult);

		// When
		JwTokenGottenResult actual = authApplicationService.getJwToken(inboundCommand);

		// Then
		assertThat(actual).isNotNull();
		assertThat(actual.token()).isEqualTo("mock-token-xyz");
		verify(authServiceClient).getJwToken(expectedOutboundCommand);
	}

	/**
	 * Feature: 取得使用者權限清單
	 * Scenario: 依據有效使用者名稱，成功將 Inbound Query 轉為 Outbound Query 向外部 AuthService 查詢權限清單
	 * Given: 包含 username 的 Query 物件 (GetPermissionListQuery)
	 * When: 呼叫 GetPermissionListUseCase.getPermissionList
	 * Then: 成功轉換為 GetPermissionListFromAuthQuery 並透過 AuthServiceClientPort 回傳 PermissionGottenResult
	 */
	@Test
	@DisplayName("成功將 Inbound Query 轉換為 Outbound Query 並取得使用者權限清單")
	void getPermissionList_success() {
		// Given
		GetPermissionListQuery inboundQuery = new GetPermissionListQuery("admin");
		GetPermissionListFromAuthQuery expectedOutboundQuery = new GetPermissionListFromAuthQuery("admin");
		PermissionGottenResult expectedResult = new PermissionGottenResult(List.of("USER_READ", "USER_WRITE"));
		when(authServiceClient.getPermissionList(expectedOutboundQuery)).thenReturn(expectedResult);

		// When
		PermissionGottenResult actual = authApplicationService.getPermissionList(inboundQuery);

		// Then
		assertThat(actual).isNotNull();
		assertThat(actual.permissionList()).containsExactly("USER_READ", "USER_WRITE");
		verify(authServiceClient).getPermissionList(expectedOutboundQuery);
	}
}
