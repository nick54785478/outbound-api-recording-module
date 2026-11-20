package com.example.demo.iface.resource.out;

/**
 * 使用者權限取得結果回應載體
 *
 * <p>符合命名規範 3-2: N + Ved + Resource，放置於 resource/out/</p>
 * <p>其內含 data 採用表現層專屬的 {@link PermissionGottenData}，與應用層 DTO 徹底解耦。</p>
 *
 * @param code 回應狀態碼
 * @param message 回應訊息
 * @param data 權限清單資料載體
 */
public record PermissionGottenResource(String code, String message, PermissionGottenData data) {
}
