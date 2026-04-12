package org.sopt.domain.post.domain.repository;

import org.sopt.domain.post.domain.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    List<Post> findAll();

    Optional<Post> findById(Long id);

    void deleteById(Long id);
}
