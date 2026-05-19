package org.sopt.global.security;

import java.time.LocalDateTime;

/**
 * 발급된 JWT와 만료 시각.
 */
public record JwtToken(
        String value,
        LocalDateTime expiresAt
) {
}
