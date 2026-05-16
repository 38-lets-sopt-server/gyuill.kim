package org.sopt.domain.post.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 게시글 수정 요청 본문.
 */
@Schema(description = "게시글 수정 요청")
public record UpdatePostRequest(
        @Schema(description = "게시글 제목", example = "수정된 제목")
        @NotBlank(message = "게시글 제목은 필수입니다.")
        @Size(max = 50, message = "게시글 제목은 50자 이하여야 합니다.")
        String title,

        @Schema(description = "게시글 본문", example = "수정된 본문입니다.")
        @NotBlank(message = "게시글 내용은 필수입니다.")
        @Size(max = 10_000, message = "게시글 내용은 10,000자 이하여야 합니다.")
        String content
) {
}
