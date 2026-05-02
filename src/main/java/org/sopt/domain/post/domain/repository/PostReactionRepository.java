package org.sopt.domain.post.domain.repository;

import org.sopt.domain.post.domain.model.PostReaction;
import org.sopt.domain.post.domain.model.ReactionType;

/**
 * 게시글 반응 저장소 추상화.
 */
public interface PostReactionRepository {

    /**
     * 게시글 반응을 저장한다.
     *
     * @param postReaction 저장할 반응
     * @return 저장된 반응
     */
    PostReaction save(PostReaction postReaction);

    /**
     * 특정 사용자 반응 존재 여부를 조회한다.
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
