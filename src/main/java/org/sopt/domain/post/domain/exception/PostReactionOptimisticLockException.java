package org.sopt.domain.post.domain.exception;

import org.sopt.domain.post.domain.model.ReactionType;
import org.sopt.global.exception.BaseException;

import java.util.Map;

public class PostReactionOptimisticLockException extends BaseException {

    public PostReactionOptimisticLockException(Long postId, Long userId, ReactionType type, int maxRetryCount) {
        super(
                PostErrorCode.POST_REACTION_CONFLICT,
                Map.of(
                        "postId", postId,
                        "userId", userId,
                        "reactionType", type.name(),
                        "maxRetryCount", maxRetryCount
                )
        );
    }
}
