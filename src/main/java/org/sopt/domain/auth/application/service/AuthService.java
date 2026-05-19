package org.sopt.domain.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.auth.application.dto.AuthTokenResult;
import org.sopt.domain.auth.domain.exception.AuthErrorCode;
import org.sopt.domain.auth.domain.model.RefreshToken;
import org.sopt.domain.auth.domain.repository.RefreshTokenRepository;
import org.sopt.domain.auth.infrastructure.RefreshTokenHasher;
import org.sopt.domain.user.domain.exception.UserErrorCode;
import org.sopt.domain.user.domain.model.User;
import org.sopt.domain.user.domain.repository.UserRepository;
import org.sopt.global.exception.BaseException;
import org.sopt.global.security.jwt.JwtToken;
import org.sopt.global.security.jwt.JwtTokenProvider;
import org.sopt.global.security.jwt.JwtTokenType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 로그인과 토큰 재발급을 담당하는 인증 서비스.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private static final String BEARER_TYPE = "Bearer";

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenHasher refreshTokenHasher;

    /**
     * 로그인 ID와 비밀번호를 검증하고 토큰을 발급한다.
     *
     * @param loginId 로그인 ID
     * @param password 평문 비밀번호
     * @return 발급된 토큰
     */
    @Transactional
    public AuthTokenResult login(String loginId, String password) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BaseException(UserErrorCode.INVALID_LOGIN_CREDENTIALS));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BaseException(UserErrorCode.INVALID_LOGIN_CREDENTIALS);
        }

        return issueAndSaveTokens(user);
    }

    /**
     * refresh token을 검증하고 새 토큰 쌍을 발급한다.
     *
     * @param refreshToken refresh token
     * @return 새로 발급된 토큰
     */
    @Transactional
    public AuthTokenResult reissue(String refreshToken) {
        Long userId = jwtTokenProvider.getUserId(refreshToken, JwtTokenType.REFRESH);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(UserErrorCode.INVALID_LOGIN_CREDENTIALS));
        RefreshToken savedRefreshToken = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new BaseException(AuthErrorCode.INVALID_REFRESH_TOKEN));
        if (!refreshTokenHasher.matches(refreshToken, savedRefreshToken.getTokenHash())
                || savedRefreshToken.isExpired()) {
            throw new BaseException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        return issueAndSaveTokens(user);
    }

    private AuthTokenResult issueAndSaveTokens(User user) {
        Long userId = user.getId();
        JwtToken accessToken = jwtTokenProvider.createAccessToken(userId);
        JwtToken refreshToken = jwtTokenProvider.createRefreshToken(userId);
        saveRefreshToken(user, refreshToken);

        return new AuthTokenResult(
                BEARER_TYPE,
                accessToken.value(),
                accessToken.expiresAt(),
                refreshToken.value(),
                refreshToken.expiresAt()
        );
    }

    private void saveRefreshToken(User user, JwtToken token) {
        LocalDateTime expiresAt = token.expiresAt();
        String tokenHash = refreshTokenHasher.hash(token.value());
        refreshTokenRepository.findByUserId(user.getId())
                .ifPresentOrElse(
                        refreshToken -> refreshToken.updateTokenHash(tokenHash, expiresAt),
                        () -> refreshTokenRepository.save(new RefreshToken(user, tokenHash, expiresAt))
                );
    }
}
