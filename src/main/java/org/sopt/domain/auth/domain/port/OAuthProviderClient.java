package org.sopt.domain.auth.domain.port;
import org.sopt.domain.auth.domain.model.OAuthProvider;

/**
 * 외부 OAuth 제공자별 토큰 검증 클라이언트.
 */
public interface OAuthProviderClient {

    /**
     * 지원하는 OAuth 제공자를 반환한다.
     *
     * @return OAuth 제공자
     */
    OAuthProvider getProvider();

    /**
     * 외부 토큰을 검증하고 사용자 프로필을 반환한다.
     *
     * @param token 외부 OAuth 토큰
     * @return 검증된 사용자 프로필
     */
    OAuthUserProfile verify(String token);
}
