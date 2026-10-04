package com.example.demo.iface.dto.res;

import com.example.demo.application.shared.outbound.auth.dto.PermissionGottenData;

public record PermissionGettenResource(String code, String message, PermissionGottenData data) {

}
