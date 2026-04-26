package org.sopt.domain.post.presentation.dto.request;

import org.sopt.domain.post.domain.exception.PostErrorCode;
import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.global.exception.BaseException;

public record GetPostsRequest(BoardType boardType, Long cursor, int size) {

    public void validate() {
        if (cursor != null && cursor < 1) {
            throw new BaseException(PostErrorCode.INVALID_PAGINATION);
        }
        if (size < 1) {
            throw new BaseException(PostErrorCode.INVALID_PAGINATION);
        }
    }
}
