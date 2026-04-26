package org.sopt.domain.post.presentation.dto.request;

import org.sopt.domain.post.domain.exception.PostErrorCode;
import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.global.exception.BaseException;

public record CreatePostRequest(BoardType boardType, String title, String content, String author) {
    private static final int MAX_TITLE_LENGTH = 100;
    private static final int MAX_CONTENT_LENGTH = 10_000;

    public void validate() {
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
        if (author == null || author.isBlank()) {
            throw new BaseException(PostErrorCode.INVALID_POST_AUTHOR);
        }
    }
}
