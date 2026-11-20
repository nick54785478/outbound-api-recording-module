package com.example.demo.iface.resource.out;

/**
 * JWT Token 表現層回應資料載體
 *
 * @param token JWT 存取權杖
 */
public record JwTokenGottenData(String token) {
}
