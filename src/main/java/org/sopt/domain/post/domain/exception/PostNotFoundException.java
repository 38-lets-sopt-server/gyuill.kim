package org.sopt.domain.post.domain.exception;

import org.sopt.global.exception.BaseException;

import java.util.Map;

public class PostNotFoundException extends BaseException {

    public PostNotFoundException(Long postId) {
        super(PostErrorCode.POST_NOT_FOUND, Map.of("postId", postId));
    }
}
