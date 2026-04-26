package org.sopt.domain.post.presentation.dto.request;

import org.sopt.domain.post.domain.exception.PostErrorCode;
import org.sopt.global.exception.BaseException;

public record UpdatePostRequest(String title, String content) {
    private static final int MAX_TITLE_LENGTH = 100;
    private static final int MAX_CONTENT_LENGTH = 10_000;

    public void validate() {
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
