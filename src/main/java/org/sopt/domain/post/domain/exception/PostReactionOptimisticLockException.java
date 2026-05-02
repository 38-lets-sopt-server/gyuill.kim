package org.sopt.domain.post.domain.exception;

import org.sopt.domain.post.domain.model.ReactionType;
import org.sopt.global.exception.BaseException;

import java.util.Map;

/**
 * 반응 집계 갱신이 재시도 한도를 넘겨 실패했을 때 발생하는 예외.
 */
public class PostReactionOptimisticLockException extends BaseException {

    /**
     * 재시도 실패 문맥을 담아 예외를 생성한다.
     *
     * @param postId 게시글 ID
     * @param userId 사용자 ID
     * @param type 반응 타입
     * @param maxRetryCount 최대 재시도 횟수
     */
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
