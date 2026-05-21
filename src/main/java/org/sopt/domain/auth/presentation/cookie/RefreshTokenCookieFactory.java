package org.sopt.domain.auth.presentation.cookie;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * refresh token을 운반하는 HttpOnly 쿠키의 생성과 만료를 담당한다.
 * 쿠키는 Path=/auth로 한정되어 인증 관련 엔드포인트에서만 자동 전송된다.
 */
@Component
public class RefreshTokenCookieFactory {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    private static final String COOKIE_PATH = "/auth";

    private final boolean secure;
    private final String sameSite;
    private final long refreshTokenValidityInMilliseconds;

    public RefreshTokenCookieFactory(
            @Value("${auth.cookie.secure}") boolean secure,
            @Value("${auth.cookie.same-site}") String sameSite,
            @Value("${jwt.refresh-token-validity-in-milliseconds}") long refreshTokenValidityInMilliseconds
    ) {
        this.secure = secure;
        this.sameSite = sameSite;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
    }

    /**
     * refresh token 값을 담은 쿠키를 생성한다.
     *
     * @param refreshToken refresh token 원문
     * @return HttpOnly · Secure · SameSite · Path=/auth 속성이 적용된 쿠키
     */
    public ResponseCookie create(String refreshToken) {
        return baseBuilder(refreshToken)
                .maxAge(Duration.ofMillis(refreshTokenValidityInMilliseconds))
                .build();
    }

    /**
     * 클라이언트의 refresh token 쿠키를 즉시 만료시키는 쿠키를 생성한다.
     * 만료 쿠키는 생성 시점과 동일한 Path/Secure/SameSite 속성을 가져야 브라우저가 같은 쿠키로 인식해 제거한다.
     *
     * @return Max-Age=0으로 만료된 쿠키
     */
    public ResponseCookie expire() {
        return baseBuilder("")
                .maxAge(0)
                .build();
    }

    private ResponseCookie.ResponseCookieBuilder baseBuilder(String value) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, value)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .path(COOKIE_PATH);
    }
}
