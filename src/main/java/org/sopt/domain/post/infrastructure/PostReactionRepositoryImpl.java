package org.sopt.domain.post.infrastructure;

import org.sopt.domain.post.domain.model.PostReaction;
import org.sopt.domain.post.domain.model.ReactionType;
import org.sopt.domain.post.domain.repository.PostReactionRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PostReactionRepositoryImpl implements PostReactionRepository {

    private final PostReactionJpaRepository postReactionJpaRepository;

    public PostReactionRepositoryImpl(PostReactionJpaRepository postReactionJpaRepository) {
        this.postReactionJpaRepository = postReactionJpaRepository;
    }

    @Override
    public PostReaction save(PostReaction postReaction) {
        return postReactionJpaRepository.save(postReaction);
    }

    @Override
    public boolean existsByPostIdAndUserIdAndType(Long postId, Long userId, ReactionType type) {
        return postReactionJpaRepository.existsByPostIdAndUserIdAndType(postId, userId, type);
    }

    @Override
    public void deleteByPostIdAndUserIdAndType(Long postId, Long userId, ReactionType type) {
        postReactionJpaRepository.deleteByPostIdAndUserIdAndType(postId, userId, type);
    }
}
