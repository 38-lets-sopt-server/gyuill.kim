package org.sopt.domain.auth.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.auth.application.dto.AuthTokenResult;

import java.time.LocalDateTime;

/**
 * 인증 토큰 응답.
 */
@Schema(description = "인증 토큰 응답")
public record AuthTokenResponse(
        @Schema(description = "토큰 타입", example = "Bearer")
        String tokenType,
        @Schema(description = "access token")
        String accessToken,
        @Schema(description = "access token 만료 시각", example = "2026-05-20T12:34:56")
        LocalDateTime accessTokenExpiresAt,
        @Schema(description = "refresh token")
        String refreshToken,
        @Schema(description = "refresh token 만료 시각", example = "2026-06-03T12:34:56")
        LocalDateTime refreshTokenExpiresAt
) {

    public static AuthTokenResponse from(AuthTokenResult result) {
        return new AuthTokenResponse(
                result.tokenType(),
                result.accessToken(),
                result.accessTokenExpiresAt(),
                result.refreshToken(),
                result.refreshTokenExpiresAt()
        );
    }
}
