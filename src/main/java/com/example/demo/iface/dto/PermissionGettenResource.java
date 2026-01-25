package com.example.demo.iface.dto;

import com.example.demo.application.shared.outbound.auth.dto.PermissionGettenData;

public record PermissionGettenResource(String code, String message, PermissionGettenData data) {

}
