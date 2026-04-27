package org.sopt.domain.post.infrastructure;

import org.sopt.domain.post.domain.model.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Post 엔티티에 대한 Spring Data JPA 저장소.
 * 단건 조회 시 작성자와 통계를 함께 읽어 서비스 계층에서 N+1이 발생하지 않도록 한다.
 */
public interface PostJpaRepository extends JpaRepository<Post, Long>, PostQueryRepository {

    /**
     * 작성자와 통계를 함께 로딩해 게시글을 조회한다.
     *
     * @param id 게시글 ID
     * @return 게시글 조회 결과
     */
    @Override
    @EntityGraph(attributePaths = {"authorUser", "stats"})
    Optional<Post> findById(Long id);
}
