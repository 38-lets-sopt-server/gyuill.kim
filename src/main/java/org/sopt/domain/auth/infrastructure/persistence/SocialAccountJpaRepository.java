package org.sopt.domain.auth.infrastructure.persistence;

import org.sopt.domain.auth.domain.model.OAuthProvider;
import org.sopt.domain.auth.domain.model.SocialAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * SocialAccount 엔티티에 대한 Spring Data JPA 저장소.
 */
public interface SocialAccountJpaRepository extends JpaRepository<SocialAccount, Long> {

    /**
     * OAuth 제공자와 제공자 사용자 ID로 소셜 계정을 조회한다.
     *
     * @param provider OAuth 제공자
     * @param providerUserId 제공자 사용자 ID
     * @return 소셜 계정 조회 결과
     */
    Optional<SocialAccount> findByProviderAndProviderUserId(OAuthProvider provider, String providerUserId);
}
