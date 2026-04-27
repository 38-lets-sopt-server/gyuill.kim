package org.sopt.domain.post.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.post.domain.exception.PostErrorCode;
import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.global.exception.BaseException;

@Schema(description = "게시글 목록 조회 요청")
/**
 * 게시글 목록 조회 요청 파라미터 모델.
 */
public record GetPostsRequest(
        @Schema(description = "게시판 타입", example = "FREE")
        BoardType boardType,
        @Schema(description = "다음 페이지 조회용 커서", example = "10", nullable = true)
        Long cursor,
        @Schema(description = "페이지 크기", example = "10")
        int size
) {
    private static final int MAX_CURSOR_SIZE = 100;

    /**
     * 커서 페이징 요청값을 검증한다.
     *
     * @throws BaseException 커서나 사이즈가 허용 범위를 벗어나는 경우
     */
    public void validate() {
        if (cursor != null && cursor < 1) {
            throw new BaseException(PostErrorCode.INVALID_PAGINATION);
        }
        if (size < 1 || size > MAX_CURSOR_SIZE) {
            throw new BaseException(PostErrorCode.INVALID_PAGINATION);
        }
    }
}
