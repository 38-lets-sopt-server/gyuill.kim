package org.sopt.domain.post.infrastructure;

import org.sopt.domain.post.domain.model.PostReaction;
import org.sopt.domain.post.domain.model.ReactionType;
import org.sopt.domain.post.domain.repository.PostReactionRepository;
import org.springframework.stereotype.Repository;

@Repository
/**
 * 게시글 반응 저장소를 JPA 구현체에 연결하는 어댑터.
 */
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
