package com.example.demo.application.shared.dto;

import java.util.List;

/**
 * 使用者權限取得結果 DTO
 *
 * <p>符合命名規範 3-6: N + Gotten + Result</p>
 *
 * @param permissionList 權限代碼清單
 */
public record PermissionGottenResult(List<String> permissionList) {
}
