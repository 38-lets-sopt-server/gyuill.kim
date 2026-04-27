package org.sopt.domain.post.domain.repository;

import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.domain.model.Post;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Slice<Post> findAllByCursor(BoardType boardType, Long cursor, int size);

    Slice<Post> search(String keyword, Long cursor, int size);

    Optional<Post> findById(Long id);

    boolean existsByAuthorUserId(Long authorUserId);

    void deleteById(Long id);
}
