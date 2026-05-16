package org.sopt.domain.post.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.sopt.domain.post.domain.model.BoardType;

/**
 * 게시글 목록 조회 요청 파라미터 모델.
 */
@Schema(description = "게시글 목록 조회 요청")
public record GetPostsRequest(
        @Schema(description = "게시판 타입", example = "FREE")
        BoardType boardType,

        @Schema(description = "다음 페이지 조회용 커서", example = "10", nullable = true)
        @Min(value = 1, message = "페이지 요청 값이 올바르지 않습니다.")
        Long cursor,

        @Schema(description = "페이지 크기", example = "10")
        @Min(value = 1, message = "페이지 요청 값이 올바르지 않습니다.")
        @Max(value = 100, message = "페이지 요청 값이 올바르지 않습니다.")
        Integer size
) {
    public GetPostsRequest {
        if (size == null) {
            size = 10;
        }
    }
}
