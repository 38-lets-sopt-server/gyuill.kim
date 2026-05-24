package org.sopt.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.post.application.port.UserPort;
import org.sopt.domain.post.domain.exception.PostNotFoundException;
import org.sopt.domain.post.domain.exception.PostReactionDuplicateException;
import org.sopt.domain.post.domain.exception.PostReactionOptimisticLockException;
import org.sopt.domain.post.domain.model.Post;
import org.sopt.domain.post.domain.model.PostReaction;
import org.sopt.domain.post.domain.model.ReactionType;
import org.sopt.domain.post.domain.repository.PostReactionRepository;
import org.sopt.domain.post.domain.repository.PostRepository;
import org.sopt.domain.user.domain.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 좋아요/스크랩 반응 1회 적용을 별도 트랜잭션으로 실행하는 전용 클래스입니다.
 *
 * 낙관적 락 충돌 시 재시도를 하려면 각 시도가 새로운 트랜잭션에서 실행되어야 합니다.
 * 하지만 같은 서비스 클래스 내부 메서드를 직접 호출하는 방식(self-invocation)으로는
 * Spring의 @Transactional 프록시를 거치지 않기 때문에 REQUIRES_NEW가 제대로 적용되지 않습니다.
 *
 * 그래서 반응 처리 1회를 별도 빈으로 분리해 새 트랜잭션에서 수행하고,
 * 재시도 정책은 Spring Retry의 @Retryable 어노테이션으로 위임했습니다.
 */
@Service
@RequiredArgsConstructor
public class PostReactionTransactionExecutor {

    private static final int POST_REACTION_MAX_RETRY_COUNT = 3;

    private final PostRepository postRepository;
    private final PostReactionRepository postReactionRepository;
    private final UserPort userPort;

    /**
     * 반응 의도 상태를 적용한다. 낙관적 락 충돌 시 새 트랜잭션에서 자동 재시도된다.
     * 목표 상태(targetReacted)를 외부에서 미리 결정해 넘기므로 재시도 중에도 멱등성을 유지한다.
     *
     * @param postId 게시글 ID
     * @param userId 사용자 ID
     * @param type 반응 타입
     * @param targetReacted 최종적으로 적용하고 싶은 반응 상태
     * @return 적용 후 반응 활성화 여부
     */
    @Retryable(
            retryFor = OptimisticLockingFailureException.class,
            maxAttempts = POST_REACTION_MAX_RETRY_COUNT,
            backoff = @Backoff(delay = 50)
    )
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean applyReactionState(Long postId, Long userId, ReactionType type, boolean targetReacted) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        post.ensureReactable();
        User user = userPort.getUser(userId);
        boolean reacted = postReactionRepository.existsByPostIdAndUserIdAndType(postId, userId, type);

        if (targetReacted) {
            applyReaction(post, user, type, reacted);
            return true;
        }

        cancelReaction(post, userId, type, reacted);
        return false;
    }

    private void applyReaction(Post post, User user, ReactionType type, boolean reacted) {
        if (reacted) {
            return;
        }
        try {
            postReactionRepository.save(new PostReaction(post, user, type));
        } catch (DataIntegrityViolationException e) {
            throw new PostReactionDuplicateException(post.getId(), user.getId(), type);
        }
        post.getStats().increaseReactionCount(type);
    }

    private void cancelReaction(Post post, Long userId, ReactionType type, boolean reacted) {
        if (reacted) {
            postReactionRepository.deleteByPostIdAndUserIdAndType(post.getId(), userId, type);
            post.getStats().decreaseReactionCount(type);
        }
    }

    /**
     * 재시도 한도를 초과한 낙관적 락 실패를 도메인 예외로 변환한다.
     * Spring Retry의 @Recover 시그니처 규약상 throwable + 원본 메서드 인자를 그대로 받아야 한다.
     */
    @Recover
    public boolean recoverOptimisticLockFailure(
            OptimisticLockingFailureException e,
            Long postId,
            Long userId,
            ReactionType type,
            boolean targetReacted
    ) {
        throw new PostReactionOptimisticLockException(postId, userId, type, POST_REACTION_MAX_RETRY_COUNT);
    }
}
