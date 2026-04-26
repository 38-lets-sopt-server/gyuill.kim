package org.sopt.domain.user.infrastructure.adapter;

import org.sopt.domain.post.domain.repository.PostRepository;
import org.sopt.domain.user.application.port.PostPort;
import org.springframework.stereotype.Component;

@Component
public class PostPortAdapter implements PostPort {

    private final PostRepository postRepository;

    public PostPortAdapter(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public boolean existsByAuthorUserId(Long authorUserId) {
        return postRepository.existsByAuthorUserId(authorUserId);
    }
}
