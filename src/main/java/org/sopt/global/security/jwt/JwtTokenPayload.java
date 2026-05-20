package org.sopt.global.security.jwt;

import java.time.LocalDateTime;

/**
 * 검증된 JWT payload에서 애플리케이션이 사용하는 값.
 */
public record JwtTokenPayload(
        Long userId,
        String tokenId,
        LocalDateTime expiresAt
) {
}
