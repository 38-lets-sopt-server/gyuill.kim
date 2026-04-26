package org.sopt.domain.post.presentation.dto.request;

import org.sopt.domain.post.domain.exception.PostErrorCode;
import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.global.exception.BaseException;

public record CreatePostRequest(BoardType boardType, String title, String content, Long authorUserId) {
    private static final int MAX_TITLE_LENGTH = 50;
    private static final int MAX_CONTENT_LENGTH = 10_000;

    public void validate() {
        // TODO: UpdatePostRequest와 검증 로직이 중복되지만 과제 범위에서는 유지하고 추후 validation annotation 도입 시 정리예정입니다.
        if (boardType == null) {
            throw new BaseException(PostErrorCode.INVALID_BOARD_TYPE);
        }
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

        // TODO: 추후 인증/인가로 변경 예정, 현재로는 dto 단에서의 검증으로 대체
        if (authorUserId == null || authorUserId < 1) {
            throw new BaseException(PostErrorCode.INVALID_POST_AUTHOR);
        }
    }
}
