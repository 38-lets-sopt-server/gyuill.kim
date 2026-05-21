package org.sopt.domain.auth.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 인증 토큰 응답. refresh token은 HttpOnly 쿠키로 운반되므로 본문에는 포함하지 않는다.
 */
@Schema(description = "인증 토큰 응답")
public record AuthTokenResponse(
        @Schema(description = "토큰 타입", example = "Bearer")
        String tokenType,
        @Schema(description = "access token")
        String accessToken,
        @Schema(description = "access token 만료 시각", example = "2026-05-20T12:34:56")
        LocalDateTime accessTokenExpiresAt
) {
}
