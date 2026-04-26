package org.sopt.domain.post.domain.repository;

import org.sopt.domain.post.domain.model.PostReaction;
import org.sopt.domain.post.domain.model.ReactionType;

public interface PostReactionRepository {

    PostReaction save(PostReaction postReaction);

    boolean existsByPostIdAndUserIdAndType(Long postId, Long userId, ReactionType type);

    void deleteByPostIdAndUserIdAndType(Long postId, Long userId, ReactionType type);
}
