package org.sopt.domain.auth.domain.port;

import org.sopt.domain.auth.domain.model.AuthTokenResult;

/**
 * Refresh Token Rotation 시 동시 요청을 흡수하기 위한 Grace Period 포트.
 */
public interface RefreshTokenGracePeriodPort {

    /**
     * 회전 시 발급된 토큰 결과를 구 토큰 해시와 함께 Grace Period 동안 보관한다.
     *
     * @param oldTokenHash 회전 전 refresh token의 HMAC 해시
     * @param result       회전 시 발급된 토큰 결과
     */
    void store(String oldTokenHash, AuthTokenResult result);

    /**
     * Grace Period 캐시에서 토큰 해시를 원자적으로 꺼내고 제거한다.
     *
     * @param oldTokenHash refresh token의 HMAC 해시
     * @return 회전 시 발급된 토큰 결과. Grace Period가 만료되었거나 이미 소비된 경우 {@code null}
     */
    AuthTokenResult consumeIfPresent(String oldTokenHash);
}
