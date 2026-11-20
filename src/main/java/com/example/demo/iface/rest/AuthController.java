package com.example.demo.iface.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.application.port.in.GetJwTokenUseCase;
import com.example.demo.application.port.in.GetPermissionListUseCase;
import com.example.demo.application.shared.dto.JwTokenGottenResult;
import com.example.demo.application.shared.dto.PermissionGottenResult;
import com.example.demo.application.shared.command.GetJwTokenCommand;
import com.example.demo.application.shared.query.GetPermissionListQuery;
import com.example.demo.iface.assembler.AuthResourceAssembler;
import com.example.demo.iface.resource.in.GetJwTokenResource;
import com.example.demo.iface.resource.out.JwTokenGottenResource;
import com.example.demo.iface.resource.out.PermissionGottenResource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

/**
 * Auth REST Controller (表現層 Inbound Adapter)
 *
 * <p>
 * 遵循 Clean Architecture 規範：
 * 接收 Request Resource，透過 Assembler 轉換為 Command/Query，
 * 呼叫 UseCase，並透過 Assembler 將 Result DTO 包裝為 Response Resource 回傳。
 * </p>
 */
@RestController
@AllArgsConstructor
@Tag(name = "Auth API", description = "測試外部 AuthService 認證與授權相關 API")
public class AuthController {

	private final GetJwTokenUseCase getJwTokenUseCase;
	private final GetPermissionListUseCase getPermissionListUseCase;
	private final AuthResourceAssembler authResourceAssembler;

	@Operation(summary = "取得 JWT Token", description = "透過帳號密碼登入取得 Token")
	@PostMapping("/login")
	public ResponseEntity<JwTokenGottenResource> getJwToken(@RequestBody GetJwTokenResource resource) {
		GetJwTokenCommand command = authResourceAssembler.toCommand(resource);
		JwTokenGottenResult data = getJwTokenUseCase.getJwToken(command);
		return new ResponseEntity<>(authResourceAssembler.toResource(data), HttpStatus.OK);
	}

	@Operation(summary = "取得使用者權限", description = "依據使用者名稱查詢權限清單")
	@GetMapping("/permission")
	public ResponseEntity<PermissionGottenResource> getPermissionList(@RequestParam String username) {
		GetPermissionListQuery query = authResourceAssembler.toQuery(username);
		PermissionGottenResult data = getPermissionListUseCase.getPermissionList(query);
		return new ResponseEntity<>(authResourceAssembler.toResource(data), HttpStatus.OK);
	}
}
