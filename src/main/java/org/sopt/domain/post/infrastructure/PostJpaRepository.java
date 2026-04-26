package org.sopt.domain.post.infrastructure;

import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.domain.model.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<Post, Long> {

    @Override
    @EntityGraph(attributePaths = "authorUser")
    Page<Post> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "authorUser")
    Page<Post> findAllByBoardType(BoardType boardType, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "authorUser")
    Optional<Post> findById(Long id);

    boolean existsByAuthorUserId(Long authorUserId);
}
