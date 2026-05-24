package org.sopt.domain.auth.domain.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.auth.domain.port.OAuthProviderClientRegistry;
import org.sopt.domain.auth.domain.port.OAuthUserProfile;
import org.sopt.domain.auth.domain.model.OAuthProvider;
import org.sopt.domain.auth.domain.model.SocialAccount;
import org.sopt.domain.auth.domain.repository.SocialAccountRepository;
import org.sopt.domain.user.domain.exception.UserErrorCode;
import org.sopt.domain.user.domain.model.User;
import org.sopt.domain.user.domain.repository.UserRepository;
import org.sopt.global.exception.BaseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * OAuth 소셜 로그인을 통한 사용자 인증과 자동 회원가입을 담당하는 서비스.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SocialLoginService {

    private final UserRepository userRepository;
    private final SocialAccountRepository socialAccountRepository;
    private final OAuthProviderClientRegistry oAuthProviderClientRegistry;

    /**
     * Google ID Token을 검증하고 소셜 계정 기준으로 사용자를 조회하거나 자동 회원가입한다.
     *
     * @param idToken Google ID Token
     * @return 인증된 사용자
     */
    public User authenticateWithGoogle(String idToken) {
        OAuthUserProfile profile = oAuthProviderClientRegistry.getClient(OAuthProvider.GOOGLE).verify(idToken);
        return socialAccountRepository.findByProviderAndProviderUserId(
                        profile.provider(),
                        profile.providerUserId()
                )
                .map(socialAccount -> {
                    socialAccount.updateProfile(profile.email(), profile.profileImageUrl());
                    User user = socialAccount.getUser();
                    if (user.isDeleted()) {
                        throw new BaseException(UserErrorCode.INVALID_LOGIN_CREDENTIALS);
                    }
                    return user;
                })
                .orElseGet(() -> registerSocialUser(profile));
    }

    private User registerSocialUser(OAuthUserProfile profile) {
        User user = userRepository.save(User.createSocialUser(profile.nickname()));
        socialAccountRepository.save(new SocialAccount(
                user,
                profile.provider(),
                profile.providerUserId(),
                profile.email(),
                profile.profileImageUrl()
        ));
        return user;
    }
}
