package org.sopt.domain.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.auth.application.dto.AuthTokenResult;
import org.sopt.domain.user.domain.exception.UserErrorCode;
import org.sopt.domain.user.domain.model.User;
import org.sopt.domain.user.domain.repository.UserRepository;
import org.sopt.global.exception.BaseException;
import org.sopt.global.security.JwtTokenProvider;
import org.sopt.global.security.JwtTokenType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 로그인과 토큰 재발급을 담당하는 인증 서비스.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private static final String BEARER_TYPE = "Bearer";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 로그인 ID와 비밀번호를 검증하고 토큰을 발급한다.
     *
     * @param loginId 로그인 ID
     * @param password 평문 비밀번호
     * @return 발급된 토큰
     */
    public AuthTokenResult login(String loginId, String password) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BaseException(UserErrorCode.INVALID_LOGIN_CREDENTIALS));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BaseException(UserErrorCode.INVALID_LOGIN_CREDENTIALS);
        }

        return issueTokens(user.getId());
    }

    /**
     * refresh token을 검증하고 새 토큰 쌍을 발급한다.
     *
     * @param refreshToken refresh token
     * @return 새로 발급된 토큰
     */
    public AuthTokenResult reissue(String refreshToken) {
        Long userId = jwtTokenProvider.getUserId(refreshToken, JwtTokenType.REFRESH);
        userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(UserErrorCode.INVALID_LOGIN_CREDENTIALS));
        return issueTokens(userId);
    }

    private AuthTokenResult issueTokens(Long userId) {
        return new AuthTokenResult(
                BEARER_TYPE,
                jwtTokenProvider.createAccessToken(userId),
                jwtTokenProvider.createRefreshToken(userId)
        );
    }
}
