package org.sopt.domain.post.domain.exception;

import org.sopt.global.exception.BaseException;

public class PostNotFoundException extends BaseException {

    public PostNotFoundException() {
        super(PostErrorCode.POST_NOT_FOUND);
    }
}
