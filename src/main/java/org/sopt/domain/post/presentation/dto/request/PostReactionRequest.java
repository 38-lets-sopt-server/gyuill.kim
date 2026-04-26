package org.sopt.domain.post.presentation.dto.request;

import org.sopt.domain.post.domain.exception.PostErrorCode;
import org.sopt.global.exception.BaseException;

public record PostReactionRequest(Long userId) {

    public void validate() {
        if (userId == null || userId < 1) {
            throw new BaseException(PostErrorCode.INVALID_POST_AUTHOR);
        }
    }
}
