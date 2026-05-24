package org.sopt.domain.auth.domain.repository;

import java.time.LocalDateTime;

/**
 * 로그아웃된 access token을 추적하는 블랙리스트 저장소 추상화.
 */
public interface AccessTokenBlacklistRepository {

    /**
     * access token의 JWT ID를 블랙리스트에 등록한다.
     *
     * @param tokenId   JWT ID (jti 클레임)
     * @param expiresAt access token 만료 시각. 구현체는 이 시각까지만 항목을 유지한다.
     */
    void add(String tokenId, LocalDateTime expiresAt);

    /**
     * JWT ID가 블랙리스트에 존재하는지 확인한다.
     *
     * @param tokenId JWT ID
     * @return 블랙리스트에 존재하면 {@code true}
     */
    boolean exists(String tokenId);
}
