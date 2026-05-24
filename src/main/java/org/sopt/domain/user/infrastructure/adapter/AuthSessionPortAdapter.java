package org.sopt.domain.user.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.auth.domain.service.AuthTokenService;
import org.sopt.domain.user.application.port.AuthSessionPort;
import org.sopt.global.security.authentication.AuthenticatedUser;
import org.sopt.global.security.authentication.UserRoleCache;
import org.springframework.stereotype.Component;

/**
 * User 도메인용 {@link AuthSessionPort} 구현체.
 * 회원 탈퇴 시 필요한 인증 토큰 회수와 역할 캐시 무효화를 인증 계층에 위임한다.
 */
@Component
@RequiredArgsConstructor
public class AuthSessionPortAdapter implements AuthSessionPort {

    private final AuthTokenService authTokenService;
    private final UserRoleCache userRoleCache;

    /**
     * 현재 access token을 블랙리스트에 등록하고 refresh token과 역할 캐시를 제거한다.
     *
     * @param authenticatedUser 인증 사용자
     */
    @Override
    public void revoke(AuthenticatedUser authenticatedUser) {
        authTokenService.revoke(authenticatedUser);
        userRoleCache.evict(authenticatedUser.userId());
    }
}
