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
import com.example.demo.application.shared.outbound.auth.dto.JwTokenGettenData;
import com.example.demo.application.shared.outbound.auth.dto.PermissionGettenData;
import com.example.demo.iface.dto.GetJwTokenResource;
import com.example.demo.iface.dto.JwTokenGettenResource;
import com.example.demo.iface.dto.PermissionGettenResource;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AuthController {

	private AuthApplicationService applicationService;

	@PostMapping("/login")
	public ResponseEntity<JwTokenGettenResource> getJwToken(@RequestBody GetJwTokenResource resource) {
		GetJwTokenCommand command = BaseDataTransformer.transformData(resource, GetJwTokenCommand.class);
		JwTokenGettenData data = applicationService.getJwToken(command);
		return new ResponseEntity<>(new JwTokenGettenResource("200", "Success", data), HttpStatus.OK);
	}

	@GetMapping("/permission")
	public ResponseEntity<PermissionGettenResource> getJwToken(@RequestParam String username) {
		PermissionGettenData data = applicationService.getPermissionList(username);
		return new ResponseEntity<>(new PermissionGettenResource("200", "Success", data), HttpStatus.OK);
	}
}
