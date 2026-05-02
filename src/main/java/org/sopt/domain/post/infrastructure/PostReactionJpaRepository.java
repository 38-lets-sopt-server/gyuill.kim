package org.sopt.domain.post.infrastructure;

import org.sopt.domain.post.domain.model.PostReaction;
import org.sopt.domain.post.domain.model.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * PostReaction 엔티티에 대한 Spring Data JPA 저장소.
 */
public interface PostReactionJpaRepository extends JpaRepository<PostReaction, Long> {

    /**
     * 특정 사용자 반응 존재 여부를 확인한다.
     *
     * @param postId 게시글 ID
     * @param userId 사용자 ID
     * @param type 반응 타입
     * @return 반응 존재 여부
     */
    boolean existsByPostIdAndUserIdAndType(Long postId, Long userId, ReactionType type);

    /**
     * 특정 사용자 반응을 삭제한다.
     *
     * @param postId 게시글 ID
     * @param userId 사용자 ID
     * @param type 반응 타입
     */
    void deleteByPostIdAndUserIdAndType(Long postId, Long userId, ReactionType type);
}
