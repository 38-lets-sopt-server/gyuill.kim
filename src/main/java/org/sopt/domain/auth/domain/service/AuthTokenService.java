package org.sopt.domain.auth.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.domain.auth.domain.model.AuthTokenResult;
import org.sopt.domain.auth.domain.exception.AuthErrorCode;
import org.sopt.domain.auth.domain.model.RefreshToken;
import org.sopt.domain.auth.domain.repository.AccessTokenBlacklistRepository;
import org.sopt.domain.auth.domain.repository.RefreshTokenRepository;
import org.sopt.domain.auth.infrastructure.RefreshTokenGracePeriodStore;
import org.sopt.domain.auth.infrastructure.RefreshTokenHasher;
import org.sopt.domain.user.domain.exception.UserErrorCode;
import org.sopt.domain.user.domain.model.User;
import org.sopt.domain.user.domain.repository.UserRepository;
import org.sopt.global.exception.BaseException;
import org.sopt.global.security.authentication.AuthenticatedUser;
import org.sopt.global.security.jwt.JwtToken;
import org.sopt.global.security.jwt.JwtTokenProvider;
import org.sopt.global.security.jwt.JwtTokenType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 토큰 발급, 회전, 회수를 담당하는 서비스.
 *
 * <p>Refresh Token Rotation + Reuse Detection + Grace Period 전략을 적용한다.
 * <ul>
 *   <li><b>Rotation</b>: 재발급 시 구 토큰을 즉시 무효화하고 새 토큰을 발급한다.</li>
 *   <li><b>Reuse Detection</b>: 이미 회전된 토큰이 다시 사용되면 해당 사용자의
 *       토큰을 전체 무효화하여 탈취를 차단한다.</li>
 *   <li><b>Grace Period</b>: 회전 직후 10초간 구 토큰을 허용하여
 *       동시 요청에 의한 정상 사용자 세션 끊김을 방지한다.</li>
 * </ul>
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthTokenService {

    private static final String BEARER_TYPE = "Bearer";

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenBlacklistRepository accessTokenBlacklistRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenHasher refreshTokenHasher;
    private final RefreshTokenGracePeriodStore gracePeriodStore;

    /**
     * 사용자에 대한 access token과 refresh token을 발급한다.
     *
     * @param user 토큰을 발급할 사용자
     * @return 발급된 토큰
     */
    public AuthTokenResult issue(User user) {
        return issueAndSaveTokens(user);
    }

    /**
     * refresh token을 검증하고 새 토큰 쌍을 발급한다.
     *
     * <p>재발급 흐름:
     * <ol>
     *   <li>현재 DB의 해시와 일치하면 정상 회전 후 구 해시를 Grace Period에 등록한다.</li>
     *   <li>DB 해시와 불일치하지만 Grace Period에 존재하면 동시 요청으로 간주해 허용한다.</li>
     *   <li>양쪽 모두 실패하면 Reuse로 판단, 사용자의 토큰을 전체 무효화한다.</li>
     * </ol>
     *
     * @param refreshToken refresh token 원문
     * @return 새로 발급된 토큰
     */
    public AuthTokenResult reissue(String refreshToken) {
        Long userId = jwtTokenProvider.getUserId(refreshToken, JwtTokenType.REFRESH);
        User user = findActiveUser(userId);
        String incomingHash = refreshTokenHasher.hash(refreshToken);

        RefreshToken savedToken = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new BaseException(AuthErrorCode.INVALID_REFRESH_TOKEN));

        if (refreshTokenHasher.matches(refreshToken, savedToken.getTokenHash())
                && !savedToken.isExpired()) {
            AuthTokenResult result = issueAndSaveTokens(user);
            gracePeriodStore.store(incomingHash, result);
            return result;
        }

        AuthTokenResult graceResult = gracePeriodStore.consumeIfPresent(incomingHash);
        if (graceResult != null) {
            return graceResult;
        }

        refreshTokenRepository.deleteByUserId(userId);
        log.warn("Refresh token reuse detected: userId={}", userId);
        throw new BaseException(AuthErrorCode.REFRESH_TOKEN_REUSE_DETECTED);
    }

    /**
     * refresh token을 삭제하고 현재 access token을 블랙리스트에 등록한다.
     *
     * @param authenticatedUser 인증된 사용자
     */
    public void revoke(AuthenticatedUser authenticatedUser) {
        refreshTokenRepository.deleteByUserId(authenticatedUser.userId());
        accessTokenBlacklistRepository.add(
                authenticatedUser.tokenId(),
                authenticatedUser.accessTokenExpiresAt()
        );
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

    private User findActiveUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(UserErrorCode.INVALID_LOGIN_CREDENTIALS));
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
