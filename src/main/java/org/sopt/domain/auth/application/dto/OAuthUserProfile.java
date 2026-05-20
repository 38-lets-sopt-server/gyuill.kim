package org.sopt.domain.auth.application.dto;

import org.sopt.domain.auth.domain.model.OAuthProvider;

/**
 * 외부 OAuth 제공자에서 검증해 가져온 사용자 프로필.
 */
public record OAuthUserProfile(
        OAuthProvider provider,
        String providerUserId,
        String email,
        String nickname,
        String profileImageUrl
) {
}
