package org.sopt.domain.post.presentation.dto.request;

import org.sopt.domain.post.domain.exception.PostErrorCode;
import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.global.exception.BaseException;

public record CreatePostRequest(BoardType boardType, String title, String content, String author) {

    public void validate() {
        if (boardType == null) {
            throw new BaseException(PostErrorCode.INVALID_BOARD_TYPE);
        }
        if (title == null || title.isBlank()) {
            throw new BaseException(PostErrorCode.INVALID_POST_TITLE);
        }
        if (content == null || content.isBlank()) {
            throw new BaseException(PostErrorCode.INVALID_POST_CONTENT);
        }
    }
}
