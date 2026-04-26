package org.sopt.domain.post.infrastructure;

import org.sopt.domain.post.domain.model.PostReaction;
import org.sopt.domain.post.domain.model.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReactionJpaRepository extends JpaRepository<PostReaction, Long> {

    boolean existsByPostIdAndUserIdAndType(Long postId, Long userId, ReactionType type);

    void deleteByPostIdAndUserIdAndType(Long postId, Long userId, ReactionType type);
}
