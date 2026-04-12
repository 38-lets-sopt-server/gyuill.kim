package org.sopt.domain.post.exception;

import org.sopt.domain.post.code.PostErrorCode;
import org.sopt.global.exception.BaseException;

public class PostNotFoundException extends BaseException {

    public PostNotFoundException() {
        super(PostErrorCode.POST_NOT_FOUND);
    }
}
