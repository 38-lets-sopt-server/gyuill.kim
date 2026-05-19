package org.sopt.domain.auth.application.dto;

/**
 * 인증 성공 후 발급된 토큰 결과.
 */
public record AuthTokenResult(
        String tokenType,
        String accessToken,
        String refreshToken
) {
}
