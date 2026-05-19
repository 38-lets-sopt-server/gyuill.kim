package org.sopt.domain.auth.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 토큰 재발급 요청 본문.
 */
@Schema(description = "토큰 재발급 요청")
public record TokenReissueRequest(
        @Schema(description = "refresh token")
        @NotBlank(message = "refresh token은 필수입니다.")
        String refreshToken
) {
}
