package org.sopt.domain.post.domain.repository;

import org.sopt.domain.post.domain.model.Post;

import java.util.List;

public interface PostRepository {

    void save(Post post);

    List<Post> findAll();

    Post findById(Long id);

    void deleteById(Long id);

    Long generateId();
}
