package org.sopt.domain.user.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 사용자 생성 요청 본문.
 */
@Schema(description = "사용자 생성 요청")
public record CreateUserRequest(
        @Schema(description = "사용자 닉네임", example = "gyuill")
        @NotBlank(message = "사용자 닉네임은 필수입니다.")
        @Size(max = 30, message = "사용자 닉네임은 30자 이하여야 합니다.")
        String nickname
) {
}
