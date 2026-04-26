package org.sopt.domain.post.infrastructure;

import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.domain.model.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<Post, Long> {

    @Override
    @EntityGraph(attributePaths = "authorUser")
    Page<Post> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "authorUser")
    Page<Post> findAllByBoardType(BoardType boardType, Pageable pageable);

    @Query(
            value = """
                    select p
                    from Post p
                    join fetch p.authorUser
                    where p.title like concat('%', :titleKeyword, '%')
                    """,
            countQuery = """
                    select count(p)
                    from Post p
                    where p.title like concat('%', :titleKeyword, '%')
                    """
    )
    Page<Post> searchByTitle(@Param("titleKeyword") String titleKeyword, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "authorUser")
    Optional<Post> findById(Long id);

    boolean existsByAuthorUserId(Long authorUserId);
}
