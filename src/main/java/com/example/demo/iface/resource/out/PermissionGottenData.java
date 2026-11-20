package com.example.demo.iface.resource.out;

import java.util.List;

/**
 * 使用者權限清單表現層回應資料載體
 *
 * @param permissionList 權限代碼清單
 */
public record PermissionGottenData(List<String> permissionList) {
}
