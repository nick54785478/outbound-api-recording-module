package com.example.demo.iface.dto.res;

import com.example.demo.application.shared.dto.PermissionGottenResult;

public record PermissionGottenResource(String code, String message, PermissionGottenResult data) {

}
