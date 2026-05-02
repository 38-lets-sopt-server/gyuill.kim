package org.sopt.domain.post.domain.exception;

import org.sopt.domain.post.domain.model.ReactionType;
import org.sopt.global.exception.BaseException;

import java.util.Map;

/**
 * 반응 저장 중 유니크 제약에 의해 중복 반응이 감지되었을 때 발생하는 예외.
 */
public class PostReactionDuplicateException extends BaseException {

    /**
     * 충돌한 반응의 문맥 정보를 담아 예외를 생성한다.
     *
     * @param postId 게시글 ID
     * @param userId 사용자 ID
     * @param type 반응 타입
     */
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
