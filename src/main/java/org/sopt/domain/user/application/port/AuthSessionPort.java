package org.sopt.domain.user.application.port;

import org.sopt.global.security.authentication.AuthenticatedUser;

/**
 * User 유스케이스가 인증 세션 회수 구현에 직접 의존하지 않도록 분리한 포트.
 */
public interface AuthSessionPort {

    /**
     * 인증된 사용자의 현재 인증 세션을 회수한다.
     *
     * @param authenticatedUser 인증 사용자
     */
    void revoke(AuthenticatedUser authenticatedUser);
}
