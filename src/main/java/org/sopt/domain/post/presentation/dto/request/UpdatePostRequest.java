package org.sopt.domain.post.presentation.dto.request;

import org.sopt.domain.post.domain.exception.PostErrorCode;
import org.sopt.global.exception.BaseException;

public record UpdatePostRequest(String title, String content) {

    public void validate() {
        if (title == null || title.isBlank()) {
            throw new BaseException(PostErrorCode.INVALID_POST_TITLE);
        }
        if (content == null || content.isBlank()) {
            throw new BaseException(PostErrorCode.INVALID_POST_CONTENT);
        }
    }
}
