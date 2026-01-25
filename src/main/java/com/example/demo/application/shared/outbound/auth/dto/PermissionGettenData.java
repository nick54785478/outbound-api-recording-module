package com.example.demo.application.shared.outbound.auth.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionGettenData {

	private List<String> permissionList;
}
