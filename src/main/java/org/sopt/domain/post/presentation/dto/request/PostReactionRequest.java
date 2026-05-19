package org.sopt.domain.post.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 게시글 반응 토글 요청 본문.
 */
@Schema(description = "게시글 반응 토글 요청")
public record PostReactionRequest(
        @Schema(description = "반응을 수행하는 사용자 ID", example = "1")
        @NotNull(message = "게시글 반응 사용자 ID는 필수입니다.")
        @Min(value = 1, message = "게시글 반응 사용자 ID는 필수입니다.")
        Long userId
) {
}
