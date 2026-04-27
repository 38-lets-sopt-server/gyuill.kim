package org.sopt.domain.post.domain.exception;

import org.sopt.domain.post.domain.model.Post;
import org.sopt.global.code.ErrorCode;
import org.sopt.global.exception.BaseException;

public abstract class PostStateException extends BaseException {

    protected PostStateException(ErrorCode errorCode, Post post) {
        super(errorCode, PostStateExceptionDetailsFactory.from(post));
    }
}
