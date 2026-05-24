package org.sopt.domain.auth.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.auth.domain.model.OAuthProvider;
import org.sopt.domain.auth.domain.model.SocialAccount;
import org.sopt.domain.auth.domain.repository.SocialAccountRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 소셜 계정 저장소를 JPA 구현체에 연결하는 어댑터.
 */
@Repository
@RequiredArgsConstructor
public class SocialAccountRepositoryImpl implements SocialAccountRepository {

    private final SocialAccountJpaRepository socialAccountJpaRepository;

    /**
     * 소셜 계정을 저장한다.
     *
     * @param socialAccount 저장할 소셜 계정
     * @return 저장된 소셜 계정
     */
    @Override
    public SocialAccount save(SocialAccount socialAccount) {
        return socialAccountJpaRepository.save(socialAccount);
    }

    /**
     * OAuth 제공자와 제공자 사용자 ID로 소셜 계정을 조회한다.
     *
     * @param provider OAuth 제공자
     * @param providerUserId 제공자 사용자 ID
     * @return 소셜 계정 조회 결과
     */
    @Override
    public Optional<SocialAccount> findByProviderAndProviderUserId(
            OAuthProvider provider,
            String providerUserId
    ) {
        return socialAccountJpaRepository.findByProviderAndProviderUserId(provider, providerUserId);
    }
}
