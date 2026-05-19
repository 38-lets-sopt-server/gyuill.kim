package org.sopt.domain.auth.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.auth.application.dto.AuthTokenResult;

/**
 * 인증 토큰 응답.
 */
@Schema(description = "인증 토큰 응답")
public record AuthTokenResponse(
        @Schema(description = "토큰 타입", example = "Bearer")
        String tokenType,
        @Schema(description = "access token")
        String accessToken,
        @Schema(description = "refresh token")
        String refreshToken
) {

    public static AuthTokenResponse from(AuthTokenResult result) {
        return new AuthTokenResponse(result.tokenType(), result.accessToken(), result.refreshToken());
    }
}
