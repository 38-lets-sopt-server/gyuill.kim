package org.sopt.domain.post.application.service;

import org.sopt.domain.post.application.dto.CreatePostCommand;
import org.sopt.domain.post.application.dto.PostResult;
import org.sopt.domain.post.application.dto.UpdatePostCommand;
import org.sopt.domain.post.application.port.UserPort;
import org.sopt.domain.post.domain.exception.PostNotFoundException;
import org.sopt.domain.post.domain.exception.PostReactionOptimisticLockException;
import org.sopt.domain.post.domain.model.Post;
import org.sopt.domain.post.domain.model.ReactionType;
import org.sopt.domain.post.domain.repository.PostReactionRepository;
import org.sopt.domain.post.domain.repository.PostRepository;
import org.sopt.domain.user.domain.model.User;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostCommandService {
    private static final int POST_REACTION_MAX_RETRY_COUNT = 3;

    private final PostRepository postRepository;
    private final PostReactionRepository postReactionRepository;
    private final PostReactionTransactionExecutor postReactionTransactionExecutor;
    private final UserPort userPort;

    public PostCommandService(
            PostRepository postRepository,
            PostReactionRepository postReactionRepository,
            PostReactionTransactionExecutor postReactionTransactionExecutor,
            UserPort userPort
    ) {
        this.postRepository = postRepository;
        this.postReactionRepository = postReactionRepository;
        this.postReactionTransactionExecutor = postReactionTransactionExecutor;
        this.userPort = userPort;
    }

    public PostResult createPost(CreatePostCommand command) {
        User authorUser = userPort.getUser(command.authorUserId());
        Post post = postRepository.save(new Post(
                command.boardType(),
                command.title(),
                command.content(),
                command.isAnonymous(),
                authorUser
        ));
        return new PostResult(
                post.getId(),
                post.getBoardType(),
                post.getTitle(),
                post.getContent(),
                post.isAnonymous(),
                post.getDisplayAuthorName(),
                post.getLikeCount(),
                post.getScrapCount(),
                post.getCreatedAt()
        );
    }

    public void updatePost(Long id, UpdatePostCommand command) {
        Post post = findPostOrThrow(id);
        post.update(command.title(), command.content());
    }

    public void deletePost(Long id) {
        findPostOrThrow(id);
        postRepository.deleteById(id);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean toggleLikePost(Long postId, Long userId) {
        return toggleReactionWithRetry(postId, userId, ReactionType.LIKE);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean toggleScrapPost(Long postId, Long userId) {
        return toggleReactionWithRetry(postId, userId, ReactionType.SCRAP);
    }

    private Post findPostOrThrow(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    /**
     * 반응 등록/취소 API를 분리하면 클라이언트가 현재 반응 상태를 정확히 알고 있어야 합니다.
     * 과제 범위에서는 인증과 상태 동기화 계층이 아직 없으므로 화면 동작에 맞춰 토글 API로 단순화 했습니다.
     * 또한 현재 반응 모델은 엔티티 수정보다 조인 엔티티의 insert/delete 성격이 강해
     * 버전 락보다는 DB 유니크 제약이 더 직접적인 중복 방어 수단에 가깝다고 느꼈지만 과제 요구사항에 좋아요 버전락이 있어서 버전락도 넣게 되었습니다.
     * 일반적인 서비스 운영 환경에서 좋아요는 락을 걸고 스크랩은 락을 안건다는건 참 부자연스럽다고 느껴집니다. 그래서 둘 다 통계 테이블로 묶어서 한번에 락을 걸어버렸습니다.
     * 다만 이 구조는 좋아요와 스크랩이 서로 독립적인 반응인데도 같은 PostStats row와 version을 공유한다는 점에서
     * 도메인적으로 완전히 자연스럽다고 보기는 어렵습니다. 한쪽 트래픽이 다른 쪽 충돌률을 높일 수 있기 때문인데요,
     * 대안으로는 좋아요/스크랩 통계 테이블을 각각 분리해 충돌 경계를 나누는 방법이 있지만
     * 이번 과제 규모에서는 통계성 카운터를 위해 1:1 테이블을 또 분리하는 설계도 다소 과하고 만약 실제 운영 환경이었다면 근본적인 설계에서 더 좋은 대안이 있다고 생각했습니다.
     * 근본적으로는 반응 저장과 집계 갱신을 이벤트 기반으로 분리하는 방식이 자연스럽지만
     * 단일 서비스 과제 범위에서 구현 복잡도와 운영 개념까지 함께 끌어오는 부담이 커 구현할 엄두가 나지 않네요.. 리뷰해주시는 분들께도 죄송하고요...
     * 따라서 현재는 과제 요구사항과 구현 복잡도의 균형을 위해 PostStats 하나에 좋아요/스크랩 집계를 함께 두고
     * @Version + 재시도 구조를 공용으로 유지하는 방향으로 정했습니다.
     * 의도 상태(shouldReact)는 루프 진입 전에 결정해 재시도 중 상태가 뒤집히지 않도록 멱등성을 보장 하게 적용한다.
     * 이후 인증/인가와 멱등 정책이 구체화되면 등록/취소 분리로 재검토해볼듯??
     *
     * @return 토글 후 반응이 활성화된 상태면 {@code true}, 해제된 상태면 {@code false}
     */
    private boolean toggleReactionWithRetry(Long postId, Long userId, ReactionType type) {
        boolean shouldReact = !postReactionRepository.existsByPostIdAndUserIdAndType(postId, userId, type);
        for (int attempt = 1; attempt <= POST_REACTION_MAX_RETRY_COUNT; attempt++) {
            try {
                return postReactionTransactionExecutor.applyReactionState(postId, userId, type, shouldReact);
            } catch (OptimisticLockingFailureException e) {
                if (attempt == POST_REACTION_MAX_RETRY_COUNT) {
                    throw new PostReactionOptimisticLockException(postId, userId, type, POST_REACTION_MAX_RETRY_COUNT);
                }
            }
        }
        throw new PostReactionOptimisticLockException(postId, userId, type, POST_REACTION_MAX_RETRY_COUNT);
    }
}
