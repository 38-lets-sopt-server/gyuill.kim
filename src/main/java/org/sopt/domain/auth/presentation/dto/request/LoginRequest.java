package org.sopt.domain.auth.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 로그인 요청 본문.
 */
@Schema(description = "로그인 요청")
public record LoginRequest(
        @Schema(description = "로그인 ID", example = "gyuill")
        @NotBlank(message = "로그인 ID는 필수입니다.")
        String loginId,

        @Schema(description = "비밀번호", example = "password1234")
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password
) {
}
