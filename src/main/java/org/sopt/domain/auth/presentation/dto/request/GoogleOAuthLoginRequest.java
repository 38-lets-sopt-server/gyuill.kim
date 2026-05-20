package org.sopt.domain.auth.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Google OAuth 로그인 요청 본문.
 */
@Schema(description = "Google OAuth 로그인 요청")
public record GoogleOAuthLoginRequest(
        @Schema(description = "Google ID Token")
        @NotBlank(message = "Google ID Token은 필수입니다.")
        String idToken
) {
}
