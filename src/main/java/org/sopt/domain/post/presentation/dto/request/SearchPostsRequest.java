package org.sopt.domain.post.presentation.dto.request;

import org.sopt.domain.post.domain.exception.PostErrorCode;
import org.sopt.global.exception.BaseException;
import org.springframework.util.StringUtils;

public record SearchPostsRequest(String keyword, Long cursor, int size) {
    private static final int MAX_PAGE_SIZE = 100;
    private static final int MAX_KEYWORD_LENGTH = 100;

    public void validate() {
        if (!StringUtils.hasText(keyword)) {
            throw new BaseException(PostErrorCode.INVALID_POST_SEARCH_KEYWORD);
        }
        if (cursor != null && cursor < 1) {
            throw new BaseException(PostErrorCode.INVALID_PAGINATION);
        }
        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new BaseException(PostErrorCode.INVALID_PAGINATION);
        }
        if (keyword.length() > MAX_KEYWORD_LENGTH) {
            throw new BaseException(PostErrorCode.INVALID_POST_SEARCH_KEYWORD_LENGTH);
        }
    }
}
