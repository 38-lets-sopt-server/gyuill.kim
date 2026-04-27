package org.sopt.domain.user.infrastructure;

import org.sopt.domain.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * User 엔티티에 대한 Spring Data JPA 저장소.
 */
public interface UserJpaRepository extends JpaRepository<User, Long> {

    /**
     * 소프트 삭제되지 않은 사용자만 전체 조회한다.
     *
     * @return 사용자 목록
     */
    List<User> findAllByDeletedAtIsNull();

    /**
     * 소프트 삭제되지 않은 사용자를 ID로 조회한다.
     *
     * @param id 사용자 ID
     * @return 사용자 조회 결과
     */
    Optional<User> findByIdAndDeletedAtIsNull(Long id);
}
