package com.example.demo.application.shared.dto;

/**
 * JWT Token 取得結果 DTO
 *
 * <p>符合命名規範 3-6: N + Gotten + Result</p>
 *
 * @param token JWT 存取權杖字串
 */
public record JwTokenGottenResult(String token) {
}
