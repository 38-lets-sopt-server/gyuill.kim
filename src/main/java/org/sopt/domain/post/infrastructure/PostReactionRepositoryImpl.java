package org.sopt.domain.post.infrastructure;

import org.sopt.domain.post.domain.model.PostReaction;
import org.sopt.domain.post.domain.model.ReactionType;
import org.sopt.domain.post.domain.repository.PostReactionRepository;
import org.springframework.stereotype.Repository;

/**
 * 게시글 반응 저장소를 JPA 구현체에 연결하는 어댑터.
 * 이 클래스도 현재 하는 역할만 보면 구현체라기보다는 JPA repository에 작업을 위임하는 어댑터에 더 가까운데요.
 * 그래서 처음에는 PostReactionRepositoryAdapter가 더 맞지 않나 고민했는데요 지금 프로젝트 안에서는 UserRepositoryImpl, PostRepositoryImpl처럼
 * RepositoryImpl 네이밍을 공통으로 쓰고 있고 향후 반응 저장소 쪽 책임이 더 늘어나면 구현체 성격이 지금보다 강해질 수도 있다고 생각했습니다.
 * 그래서 지금은 역할만 보고 이름을 바꾸기보다는 프로젝트 전반의 네이밍 일관성을 유지하는 쪽으로 두었습니다.
 */
@Repository
public class PostReactionRepositoryImpl implements PostReactionRepository {

    private final PostReactionJpaRepository postReactionJpaRepository;

    public PostReactionRepositoryImpl(PostReactionJpaRepository postReactionJpaRepository) {
        this.postReactionJpaRepository = postReactionJpaRepository;
    }

    /**
     * 게시글 반응을 저장한다.
     *
     * @param postReaction 저장할 반응
     * @return 저장된 반응
     */
    @Override
    public PostReaction save(PostReaction postReaction) {
        return postReactionJpaRepository.save(postReaction);
    }

    /**
     * 특정 사용자 반응 존재 여부를 조회한다.
     *
     * @param postId 게시글 ID
     * @param userId 사용자 ID
     * @param type 반응 타입
     * @return 반응 존재 여부
     */
    @Override
    public boolean existsByPostIdAndUserIdAndType(Long postId, Long userId, ReactionType type) {
        return postReactionJpaRepository.existsByPostIdAndUserIdAndType(postId, userId, type);
    }

    /**
     * 특정 사용자 반응을 삭제한다.
     *
     * @param postId 게시글 ID
     * @param userId 사용자 ID
     * @param type 반응 타입
     */
    @Override
    public void deleteByPostIdAndUserIdAndType(Long postId, Long userId, ReactionType type) {
        postReactionJpaRepository.deleteByPostIdAndUserIdAndType(postId, userId, type);
    }
}
