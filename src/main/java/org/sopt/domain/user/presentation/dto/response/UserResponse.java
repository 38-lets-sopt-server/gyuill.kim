package org.sopt.domain.user.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "사용자 응답")
/**
 * 사용자 API 응답 모델.
 */
public record UserResponse(
        @Schema(description = "사용자 ID", example = "1")
        Long id,
        @Schema(description = "사용자 닉네임", example = "gyuill")
        String nickname,
        @Schema(description = "생성 시각", example = "2026-04-27T16:00:00")
        LocalDateTime createdAt,
        @Schema(description = "수정 시각", example = "2026-04-27T16:05:00")
        LocalDateTime updatedAt
) {
}
