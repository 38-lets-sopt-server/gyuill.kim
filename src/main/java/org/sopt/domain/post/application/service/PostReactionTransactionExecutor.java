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
 * 게시글 반응(좋아요/스크랩) 1회 시도를 독립 트랜잭션으로 실행하는 전용 실행기.
 * 낙관적 락 충돌 시 같은 서비스 내부 self-invocation으로는 REQUIRES_NEW가 적용되지 않으므로
 * 재시도 가능한 새 트랜잭션 경계를 만들기 위해 별도 빈으로 분리했다.
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
     * 반응 의도 상태를 idempotent 하게 적용해 재시도 중 상태가 뒤집히지 않도록 한다.
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
