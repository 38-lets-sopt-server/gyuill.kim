package org.sopt.domain.auth.domain.repository;

import org.sopt.domain.auth.domain.model.RefreshToken;

import java.util.Optional;

/**
 * refresh token 저장소 추상화.
 */
public interface RefreshTokenRepository {

    /**
     * refresh token을 저장한다.
     *
     * @param refreshToken 저장할 refresh token
     * @return 저장된 refresh token
     */
    RefreshToken save(RefreshToken refreshToken);

    /**
     * 사용자 ID로 refresh token을 조회한다.
     *
     * @param userId 사용자 ID
     * @return refresh token 조회 결과
     */
    Optional<RefreshToken> findByUserId(Long userId);
}
