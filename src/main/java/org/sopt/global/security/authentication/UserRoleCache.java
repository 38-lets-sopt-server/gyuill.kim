package org.sopt.global.security.authentication;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.sopt.domain.user.domain.model.User;
import org.sopt.domain.user.domain.model.UserRole;
import org.sopt.domain.user.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

/**
 * 인증 필터에서 사용하는 사용자 역할 캐시.
 * 매 요청마다 DB를 조회하는 비용을 절감한다.
 */
@Component
public class UserRoleCache {

    private final UserRepository userRepository;

    private final Cache<Long, Optional<UserRole>> cache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(Duration.ofMinutes(5))
            .build();

    public UserRoleCache(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 사용자 역할을 조회한다. 캐시에 없으면 DB에서 로드한다.
     *
     * @param userId 사용자 ID
     * @return 사용자 역할. 사용자가 존재하지 않으면 {@code null}
     */
    public UserRole getRole(Long userId) {
        return cache.get(userId, this::loadUserRole)
                .orElse(null);
    }

    /**
     * 사용자 역할 캐시에서 해당 사용자를 제거한다.
     * 역할 변경이나 사용자 삭제 시 호출하여 캐시 정합성을 유지한다.
     *
     * @param userId 캐시에서 제거할 사용자 ID
     */
    public void evict(Long userId) {
        cache.invalidate(userId);
    }

    private Optional<UserRole> loadUserRole(Long userId) {
        return userRepository.findById(userId)
                .map(User::getRole);
    }
}
