package org.sopt.domain.post.application.service;

import org.sopt.domain.post.application.port.UserPort;
import org.sopt.domain.post.domain.exception.PostNotFoundException;
import org.sopt.domain.post.domain.exception.PostReactionDuplicateException;
import org.sopt.domain.post.domain.model.Post;
import org.sopt.domain.post.domain.model.PostReaction;
import org.sopt.domain.post.domain.model.ReactionType;
import org.sopt.domain.post.domain.repository.PostReactionRepository;
import org.sopt.domain.post.domain.repository.PostRepository;
import org.sopt.domain.user.domain.model.User;
import org.springframework.dao.DataIntegrityViolationException;
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
 * 그래서 반응 처리 1회를 별도 빈으로 분리했고
 * 바깥 서비스는 retry만 담당하고 실제 DB 반영은 이 클래스가 새 트랜잭션에서 수행합니다.
 */
@Service
public class PostReactionTransactionExecutor {

    private final PostRepository postRepository;
    private final PostReactionRepository postReactionRepository;
    private final UserPort userPort;

    public PostReactionTransactionExecutor(
            PostRepository postRepository,
            PostReactionRepository postReactionRepository,
            UserPort userPort
    ) {
        this.postRepository = postRepository;
        this.postReactionRepository = postReactionRepository;
        this.userPort = userPort;
    }

    /**
     * 외부 재시도 루프에서 호출되는 단일 시도용 트랜잭션.
     * 반응 의도 상태를 (멱등)idempotent 하게 적용해 재시도 중 상태가 뒤집히지 않도록 한다.
     *
     * @param postId 게시글 ID
     * @param userId 사용자 ID
     * @param type 반응 타입
     * @param shouldReact 최종적으로 적용하고 싶은 반응 상태
     * @return 적용 후 반응 활성화 여부
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean applyReactionState(Long postId, Long userId, ReactionType type, boolean shouldReact) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        post.ensureReactable();
        User user = userPort.getUser(userId);
        boolean reacted = postReactionRepository.existsByPostIdAndUserIdAndType(postId, userId, type);

        if (shouldReact) {
            if (!reacted) {
                try {
                    postReactionRepository.save(new PostReaction(post, user, type));
                } catch (DataIntegrityViolationException e) {
                    throw new PostReactionDuplicateException(postId, userId, type);
                }
                post.initializeStats().increaseReactionCount(type);
            }
            return true;
        }

        if (reacted) {
            postReactionRepository.deleteByPostIdAndUserIdAndType(postId, userId, type);
            post.initializeStats().decreaseReactionCount(type);
        }
        return false;
    }
}
