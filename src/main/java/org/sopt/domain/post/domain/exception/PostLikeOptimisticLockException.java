package org.sopt.domain.post.domain.exception;

import org.sopt.global.exception.BaseException;

import java.util.Map;

public class PostLikeOptimisticLockException extends BaseException {

    public PostLikeOptimisticLockException(Long postId, Long userId, int maxRetryCount) {
        super(
                PostErrorCode.POST_LIKE_CONFLICT,
                Map.of(
                        "postId", postId,
                        "userId", userId,
                        "maxRetryCount", maxRetryCount
                )
        );
    }
}
