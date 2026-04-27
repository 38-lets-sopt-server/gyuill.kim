package org.sopt.domain.post.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.post.domain.exception.PostErrorCode;
import org.sopt.global.exception.BaseException;

@Schema(description = "게시글 수정 요청")
public record UpdatePostRequest(
        @Schema(description = "게시글 제목", example = "수정된 제목")
        String title,
        @Schema(description = "게시글 본문", example = "수정된 본문입니다.")
        String content
) {
    private static final int MAX_TITLE_LENGTH = 50;
    private static final int MAX_CONTENT_LENGTH = 10_000;

    public void validate() {
        // TODO: CreatePostRequest와 검증 로직이 중복되지만 과제 범위에서는 유지하고 추후 validation annotation 도입 시 정리 예정입니다.
        if (title == null || title.isBlank()) {
            throw new BaseException(PostErrorCode.INVALID_POST_TITLE);
        }
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new BaseException(PostErrorCode.INVALID_POST_TITLE_LENGTH);
        }
        if (content == null || content.isBlank()) {
            throw new BaseException(PostErrorCode.INVALID_POST_CONTENT);
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new BaseException(PostErrorCode.INVALID_POST_CONTENT_LENGTH);
        }
    }
}
