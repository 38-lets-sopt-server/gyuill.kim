package org.sopt.domain.auth.infrastructure;

import org.sopt.domain.auth.domain.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * RefreshToken 엔티티에 대한 Spring Data JPA 저장소.
 */
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * 사용자 ID로 refresh token을 조회한다.
     *
     * @param userId 사용자 ID
     * @return refresh token 조회 결과
     */
    Optional<RefreshToken> findByUser_Id(Long userId);

    /**
     * 사용자 ID로 refresh token을 삭제한다.
     *
     * @param userId 사용자 ID
     */
    void deleteByUser_Id(Long userId);
}
