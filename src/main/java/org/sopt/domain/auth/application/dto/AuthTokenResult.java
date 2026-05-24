package org.sopt.domain.auth.application.dto;

import java.time.LocalDateTime;

/**
 * 인증 성공 후 발급된 토큰 결과.
 */
public record AuthTokenResult(
        String tokenType,
        String accessToken,
        LocalDateTime accessTokenExpiresAt,
        String refreshToken,
        LocalDateTime refreshTokenExpiresAt
) {
}
