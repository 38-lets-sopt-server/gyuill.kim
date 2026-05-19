package org.sopt.domain.post.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.sopt.domain.post.domain.model.BoardType;

/**
 * 게시글 생성 요청 본문.
 */
@Schema(description = "게시글 생성 요청")
public record CreatePostRequest(
        @Schema(description = "게시판 타입", example = "FREE")
        @NotNull(message = "게시판 종류는 필수입니다.")
        BoardType boardType,

        @Schema(description = "게시글 제목", example = "점심 메뉴 추천 받습니다")
        @NotBlank(message = "게시글 제목은 필수입니다.")
        @Size(max = 50, message = "게시글 제목은 50자 이하여야 합니다.")
        String title,

        @Schema(description = "게시글 본문", example = "학교 근처에서 먹을만한 곳 있나요?")
        @NotBlank(message = "게시글 내용은 필수입니다.")
        @Size(max = 10_000, message = "게시글 내용은 10,000자 이하여야 합니다.")
        String content,

        @Schema(description = "작성자 사용자 ID", example = "1")
        @NotNull(message = "게시글 작성자 ID는 필수입니다.")
        @Min(value = 1, message = "게시글 작성자 ID는 필수입니다.")
        Long authorUserId,

        @Schema(description = "익명 여부", example = "true")
        boolean isAnonymous
) {
}
