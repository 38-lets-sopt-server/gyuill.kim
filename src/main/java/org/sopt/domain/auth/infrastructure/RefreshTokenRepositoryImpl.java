package org.sopt.domain.auth.infrastructure;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.auth.domain.model.RefreshToken;
import org.sopt.domain.auth.domain.repository.RefreshTokenRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * refresh token 저장소를 JPA 구현체에 연결하는 어댑터.
 */
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    /**
     * refresh token을 저장한다.
     *
     * @param refreshToken 저장할 refresh token
     * @return 저장된 refresh token
     */
    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenJpaRepository.save(refreshToken);
    }

    /**
     * 사용자 ID로 refresh token을 조회한다.
     *
     * @param userId 사용자 ID
     * @return refresh token 조회 결과
     */
    @Override
    public Optional<RefreshToken> findByUserId(Long userId) {
        return refreshTokenJpaRepository.findByUser_Id(userId);
    }
}
