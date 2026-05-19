package org.sopt.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

/**
 * JJWT 기반 JWT 생성/검증 컴포넌트.
 */
@Component
public class JwtTokenProvider {

    private static final String TOKEN_TYPE_CLAIM = "typ";

    private final String issuer;
    private final SecretKey secretKey;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;
    private final long clockSkewInSeconds;

    public JwtTokenProvider(
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-in-milliseconds}") long accessTokenValidityInMilliseconds,
            @Value("${jwt.refresh-token-validity-in-milliseconds}") long refreshTokenValidityInMilliseconds,
            @Value("${jwt.clock-skew-in-seconds}") long clockSkewInSeconds
    ) {
        if (!StringUtils.hasText(issuer)) {
            throw new IllegalArgumentException("JWT issuer must not be blank.");
        }
        this.issuer = issuer;
        this.secretKey = createSecretKey(secret);
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
        this.clockSkewInSeconds = clockSkewInSeconds;
    }

    /**
     * access token을 생성한다.
     *
     * @param userId 사용자 ID
     * @return access token
     */
    public JwtToken createAccessToken(Long userId) {
        return createToken(userId, JwtTokenType.ACCESS, accessTokenValidityInMilliseconds);
    }

    /**
     * refresh token을 생성한다.
     *
     * @param userId 사용자 ID
     * @return refresh token
     */
    public JwtToken createRefreshToken(Long userId) {
        return createToken(userId, JwtTokenType.REFRESH, refreshTokenValidityInMilliseconds);
    }

    /**
     * 토큰을 검증하고 subject의 사용자 ID를 반환한다.
     *
     * @param token JWT
     * @param expectedType 기대하는 토큰 타입
     * @return 사용자 ID
     */
    public Long getUserId(String token, JwtTokenType expectedType) {
        Claims claims = validateClaims(token, expectedType);
        return Long.valueOf(claims.getSubject());
    }

    private JwtToken createToken(Long userId, JwtTokenType tokenType, long validityInMilliseconds) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusMillis(validityInMilliseconds);
        String token = Jwts.builder()
                .issuer(issuer)
                .subject(String.valueOf(userId))
                .id(UUID.randomUUID().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .claim(TOKEN_TYPE_CLAIM, tokenType.name())
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();

        return new JwtToken(token, toLocalDateTime(expiresAt));
    }

    private Claims validateClaims(String token, JwtTokenType expectedType) {
        if (!StringUtils.hasText(token)) {
            throw new JwtAuthenticationException("Token is empty.");
        }

        try {
            Claims claims = Jwts.parser()
                    .requireIssuer(issuer)
                    .verifyWith(secretKey)
                    .clockSkewSeconds(clockSkewInSeconds)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String tokenType = claims.get(TOKEN_TYPE_CLAIM, String.class);
            if (!expectedType.name().equals(tokenType)) {
                throw new JwtAuthenticationException("Token type is invalid.");
            }
            return claims;
        } catch (JwtAuthenticationException e) {
            throw e;
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("Token is invalid.");
        }
    }

    private SecretKey createSecretKey(String secret) {
        if (!StringUtils.hasText(secret)) {
            throw new IllegalArgumentException("JWT secret must not be blank.");
        }
        try {
            return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        } catch (WeakKeyException e) {
            throw new IllegalArgumentException("JWT secret must be at least 256 bits for HS256.", e);
        }
    }

    private LocalDateTime toLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
