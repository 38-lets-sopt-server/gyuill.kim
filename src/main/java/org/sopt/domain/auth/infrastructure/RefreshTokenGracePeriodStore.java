package org.sopt.domain.auth.infrastructure;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Refresh Token Rotation 시 동시 요청을 흡수하기 위한 Grace Period 저장소.
 *
 * <p>회전 직후 구 토큰의 해시를 짧은 TTL(10초)로 보관한다.
 * Grace Period 내에 같은 구 토큰으로 재발급 요청이 들어오면
 * {@link #consumeIfPresent(String)}가 원자적으로 값을 반환하고 키를 제거하여
 * 이중 발급을 방지한다.</p>
 */
@Component
public class RefreshTokenGracePeriodStore {

    private final Cache<String, Long> cache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofSeconds(10))
            .maximumSize(10_000)
            .build();

    /**
     * 회전된 구 토큰의 해시를 Grace Period 동안 보관한다.
     *
     * @param tokenHash 회전 전 refresh token의 HMAC 해시
     * @param userId    토큰 소유 사용자 ID
     */
    public void store(String tokenHash, Long userId) {
        cache.put(tokenHash, userId);
    }

    /**
     * Grace Period 캐시에서 토큰 해시를 원자적으로 꺼내고 제거한다.
     *
     * @param tokenHash refresh token의 HMAC 해시
     * @return 사용자 ID. Grace Period가 만료되었거나 이미 소비된 경우 {@code null}
     */
    public Long consumeIfPresent(String tokenHash) {
        return cache.asMap().remove(tokenHash);
    }
}
