package org.sopt.domain.auth.domain.repository;

import org.sopt.domain.auth.domain.model.OAuthProvider;
import org.sopt.domain.auth.domain.model.SocialAccount;

import java.util.Optional;

/**
 * 소셜 계정 저장소 추상화.
 */
public interface SocialAccountRepository {

    /**
     * 소셜 계정을 저장한다.
     *
     * @param socialAccount 저장할 소셜 계정
     * @return 저장된 소셜 계정
     */
    SocialAccount save(SocialAccount socialAccount);

    /**
     * OAuth 제공자와 제공자 사용자 ID로 소셜 계정을 조회한다.
     *
     * @param provider OAuth 제공자
     * @param providerUserId 제공자 사용자 ID
     * @return 소셜 계정 조회 결과
     */
    Optional<SocialAccount> findByProviderAndProviderUserId(OAuthProvider provider, String providerUserId);
}
