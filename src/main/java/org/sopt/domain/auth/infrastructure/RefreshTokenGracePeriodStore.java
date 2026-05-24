package org.sopt.domain.auth.infrastructure;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.sopt.domain.auth.domain.model.AuthTokenResult;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Refresh Token Rotation 시 동시 요청을 흡수하기 위한 Grace Period 저장소.
 *
 * <p>회전 직후 구 토큰의 해시와 발급된 토큰 결과를 짧은 TTL(10초)로 보관한다.
 * Grace Period 내에 같은 구 토큰으로 재발급 요청이 들어오면
 * {@link #consumeIfPresent(String)}가 원자적으로 값을 반환하고 키를 제거하여
 * 재회전 없이 동일한 토큰을 반환한다.</p>
 */
@Component
public class RefreshTokenGracePeriodStore {

    private final Cache<String, AuthTokenResult> cache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofSeconds(10))
            .maximumSize(10_000)
            .build();

    /**
     * 회전 시 발급된 토큰 결과를 구 토큰 해시와 함께 Grace Period 동안 보관한다.
     *
     * @param oldTokenHash 회전 전 refresh token의 HMAC 해시
     * @param result       회전 시 발급된 토큰 결과
     */
    public void store(String oldTokenHash, AuthTokenResult result) {
        cache.put(oldTokenHash, result);
    }

    /**
     * Grace Period 캐시에서 토큰 해시를 원자적으로 꺼내고 제거한다.
     * 반환된 토큰은 정상 회전 시 발급된 것과 동일하므로 재회전이 발생하지 않는다.
     *
     * @param oldTokenHash refresh token의 HMAC 해시
     * @return 회전 시 발급된 토큰 결과. Grace Period가 만료되었거나 이미 소비된 경우 {@code null}
     */
    public AuthTokenResult consumeIfPresent(String oldTokenHash) {
        return cache.asMap().remove(oldTokenHash);
    }
}
