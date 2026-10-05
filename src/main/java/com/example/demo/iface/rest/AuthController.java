package com.example.demo.iface.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.application.service.AuthApplicationService;
import com.example.demo.application.shared.outbound.auth.command.GetJwTokenCommand;
import com.example.demo.application.shared.dto.JwTokenGottenResult;
import com.example.demo.application.shared.dto.PermissionGottenResult;
import com.example.demo.iface.dto.req.GetJwTokenResource;
import com.example.demo.iface.dto.res.JwTokenGottenResource;
import com.example.demo.iface.dto.res.PermissionGottenResource;
import com.example.demo.infra.util.BaseDataTransformer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@Tag(name = "Auth API", description = "測試外部 AuthService 認證與授權相關 API")
public class AuthController {

	private AuthApplicationService applicationService;

	@Operation(summary = "取得 JWT Token", description = "透過帳號密碼登入取得 Token")
	@PostMapping("/login")
	public ResponseEntity<JwTokenGottenResource> getJwToken(@RequestBody GetJwTokenResource resource) {
		GetJwTokenCommand command = BaseDataTransformer.transformData(resource, GetJwTokenCommand.class);
		JwTokenGottenResult data = applicationService.getJwToken(command);
		return new ResponseEntity<>(new JwTokenGottenResource("200", "Success", data), HttpStatus.OK);
	}

	@Operation(summary = "取得使用者權限", description = "依據使用者名稱查詢權限清單")
	@GetMapping("/permission")
	public ResponseEntity<PermissionGottenResource> getJwToken(@RequestParam String username) {
		PermissionGottenResult data = applicationService.getPermissionList(username);
		return new ResponseEntity<>(new PermissionGottenResource("200", "Success", data), HttpStatus.OK);
	}
}
