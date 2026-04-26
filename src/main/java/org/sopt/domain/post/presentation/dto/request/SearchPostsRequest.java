package org.sopt.domain.post.presentation.dto.request;

import org.sopt.domain.post.domain.exception.PostErrorCode;
import org.sopt.global.exception.BaseException;
import org.springframework.util.StringUtils;

public record SearchPostsRequest(String titleKeyword, String authorNickname, int page, int size) {

    public void validate() {
        if (!StringUtils.hasText(titleKeyword) && !StringUtils.hasText(authorNickname)) {
            throw new BaseException(PostErrorCode.INVALID_POST_SEARCH_CONDITION);
        }
        if (page < 0 || size < 1) {
            throw new BaseException(PostErrorCode.INVALID_PAGINATION);
        }
    }
}
