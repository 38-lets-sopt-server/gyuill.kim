package org.sopt.domain.post.infrastructure;

import org.sopt.domain.post.domain.model.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<Post, Long>, PostQueryRepository {

    @Override
    @EntityGraph(attributePaths = {"authorUser", "stats"})
    Optional<Post> findById(Long id);
}
