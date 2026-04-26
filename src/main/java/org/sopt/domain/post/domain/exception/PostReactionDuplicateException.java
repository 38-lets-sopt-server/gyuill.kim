package org.sopt.domain.post.domain.exception;

import org.sopt.domain.post.domain.model.ReactionType;
import org.sopt.global.exception.BaseException;

import java.util.Map;

public class PostReactionDuplicateException extends BaseException {

    public PostReactionDuplicateException(Long postId, Long userId, ReactionType type) {
        super(
                PostErrorCode.POST_REACTION_CONFLICT,
                Map.of(
                        "postId", postId,
                        "userId", userId,
                        "reactionType", type.name()
                )
        );
    }
}
