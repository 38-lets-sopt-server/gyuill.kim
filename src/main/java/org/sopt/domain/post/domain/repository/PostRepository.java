package org.sopt.domain.post.domain.repository;

import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.domain.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Page<Post> findAll(Pageable pageable);

    Page<Post> findAllByBoardType(BoardType boardType, Pageable pageable);

    Optional<Post> findById(Long id);

    boolean existsByAuthorUserId(Long authorUserId);

    void deleteById(Long id);
}
