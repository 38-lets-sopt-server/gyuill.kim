package org.sopt.global.security.authentication;

import org.sopt.domain.user.domain.model.UserRole;

import java.time.LocalDateTime;

/**
 * JWT 인증이 끝난 뒤 컨트롤러에서 사용할 인증 사용자 정보.
 */
public record AuthenticatedUser(
        Long userId,
        UserRole role,
        String tokenId,
        LocalDateTime accessTokenExpiresAt
) {
}
