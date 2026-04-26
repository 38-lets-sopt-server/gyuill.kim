package org.sopt.domain.post.presentation.dto.request;

import org.sopt.domain.post.domain.exception.PostErrorCode;
import org.sopt.global.exception.BaseException;

public record SearchPostsRequest(String titleKeyword, int page, int size) {

    public void validate() {
        if (titleKeyword == null || titleKeyword.isBlank()) {
            throw new BaseException(PostErrorCode.INVALID_POST_TITLE);
        }
        if (page < 0 || size < 1) {
            throw new BaseException(PostErrorCode.INVALID_PAGINATION);
        }
    }
}
