package org.sopt.domain.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.auth.application.dto.AuthTokenResult;
import org.sopt.domain.user.domain.exception.UserErrorCode;
import org.sopt.domain.user.domain.model.User;
import org.sopt.domain.user.domain.repository.UserRepository;
import org.sopt.global.exception.BaseException;
import org.sopt.global.security.authentication.AuthenticatedUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 자격증명 검증과 인증 흐름 조율을 담당하는 서비스.
 * 토큰 생명주기는 {@link AuthTokenService}, 소셜 로그인은 {@link SocialLoginService}에 위임한다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenService authTokenService;
    private final SocialLoginService socialLoginService;

    /**
     * 로그인 ID와 비밀번호를 검증하고 토큰을 발급한다.
     *
     * @param loginId  로그인 ID
     * @param password 평문 비밀번호
     * @return 발급된 토큰
     */
    public AuthTokenResult login(String loginId, String password) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BaseException(UserErrorCode.INVALID_LOGIN_CREDENTIALS));
        if (!user.hasPassword()) {
            throw new BaseException(UserErrorCode.INVALID_LOGIN_CREDENTIALS);
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BaseException(UserErrorCode.INVALID_LOGIN_CREDENTIALS);
        }

        return authTokenService.issue(user);
    }

    /**
     * Google ID Token을 검증하고 소셜 계정 기준으로 로그인 또는 자동 회원가입을 처리한다.
     *
     * @param idToken Google ID Token
     * @return 발급된 토큰
     */
    public AuthTokenResult loginWithGoogle(String idToken) {
        User user = socialLoginService.authenticateWithGoogle(idToken);
        return authTokenService.issue(user);
    }

    /**
     * refresh token을 검증하고 새 토큰 쌍을 발급한다.
     *
     * @param refreshToken refresh token 원문
     * @return 새로 발급된 토큰
     */
    public AuthTokenResult reissue(String refreshToken) {
        return authTokenService.reissue(refreshToken);
    }

    /**
     * refresh token을 삭제하고 현재 access token을 블랙리스트에 등록한다.
     *
     * @param authenticatedUser 인증된 사용자
     */
    public void logout(AuthenticatedUser authenticatedUser) {
        authTokenService.revoke(authenticatedUser);
    }
}
