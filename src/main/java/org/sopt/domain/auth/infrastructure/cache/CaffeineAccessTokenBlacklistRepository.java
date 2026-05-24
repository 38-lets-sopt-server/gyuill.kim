package org.sopt.domain.auth.infrastructure.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import org.sopt.domain.auth.domain.repository.AccessTokenBlacklistRepository;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Caffeine 로컬 캐시 기반 access token 블랙리스트 구현체.
 *
 * <p>각 항목의 TTL을 토큰의 잔여 유효 시간에 정렬하여,
 * 토큰이 자연 만료되는 시점에 블랙리스트 항목도 함께 제거된다.</p>
 */
@Repository
public class CaffeineAccessTokenBlacklistRepository implements AccessTokenBlacklistRepository {

    private final Cache<String, LocalDateTime> cache = Caffeine.newBuilder()
            .expireAfter(new Expiry<String, LocalDateTime>() {
                @Override
                public long expireAfterCreate(String key, LocalDateTime expiresAt, long currentTime) {
                    Duration remaining = Duration.between(LocalDateTime.now(), expiresAt);
                    return remaining.isNegative() ? 0 : remaining.toNanos();
                }

                @Override
                public long expireAfterUpdate(String key, LocalDateTime expiresAt, long currentTime, long currentDuration) {
                    return currentDuration;
                }

                @Override
                public long expireAfterRead(String key, LocalDateTime expiresAt, long currentTime, long currentDuration) {
                    return currentDuration;
                }
            })
            .build();

    /** {@inheritDoc} */
    @Override
    public void add(String tokenId, LocalDateTime expiresAt) {
        cache.put(tokenId, expiresAt);
    }

    /** {@inheritDoc} */
    @Override
    public boolean exists(String tokenId) {
        return cache.getIfPresent(tokenId) != null;
    }
}
