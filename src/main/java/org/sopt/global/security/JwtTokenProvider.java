package org.sopt.global.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 외부 JWT 라이브러리 없이 HMAC-SHA256 기반 JWT를 생성하고 검증한다.
 */
@Component
public class JwtTokenProvider {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String TOKEN_TYPE_CLAIM = "typ";
    private static final String SUBJECT_CLAIM = "sub";
    private static final String ISSUED_AT_CLAIM = "iat";
    private static final String EXPIRES_AT_CLAIM = "exp";

    private final ObjectMapper objectMapper;
    private final byte[] secret;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;

    public JwtTokenProvider(
            ObjectMapper objectMapper,
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-in-milliseconds}") long accessTokenValidityInMilliseconds,
            @Value("${jwt.refresh-token-validity-in-milliseconds}") long refreshTokenValidityInMilliseconds
    ) {
        this.objectMapper = objectMapper;
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
    }

    /**
     * access token을 생성한다.
     *
     * @param userId 사용자 ID
     * @return access token
     */
    public String createAccessToken(Long userId) {
        return createToken(userId, JwtTokenType.ACCESS, accessTokenValidityInMilliseconds);
    }

    /**
     * refresh token을 생성한다.
     *
     * @param userId 사용자 ID
     * @return refresh token
     */
    public String createRefreshToken(Long userId) {
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
        Map<String, Object> claims = parseClaims(token);
        String tokenType = String.valueOf(claims.get(TOKEN_TYPE_CLAIM));
        if (!expectedType.name().equals(tokenType)) {
            throw new JwtAuthenticationException("Token type is invalid.");
        }

        long expiresAt = ((Number) claims.get(EXPIRES_AT_CLAIM)).longValue();
        if (expiresAt < Instant.now().getEpochSecond()) {
            throw new JwtAuthenticationException("Token is expired.");
        }

        return Long.valueOf(String.valueOf(claims.get(SUBJECT_CLAIM)));
    }

    private String createToken(Long userId, JwtTokenType tokenType, long validityInMilliseconds) {
        Instant now = Instant.now();
        Map<String, Object> header = Map.of(
                "alg", "HS256",
                "typ", "JWT"
        );
        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put(SUBJECT_CLAIM, String.valueOf(userId));
        claims.put(TOKEN_TYPE_CLAIM, tokenType.name());
        claims.put(ISSUED_AT_CLAIM, now.getEpochSecond());
        claims.put(EXPIRES_AT_CLAIM, now.plusMillis(validityInMilliseconds).getEpochSecond());

        String encodedHeader = base64UrlEncode(writeJson(header));
        String encodedPayload = base64UrlEncode(writeJson(claims));
        String unsignedToken = encodedHeader + "." + encodedPayload;
        return unsignedToken + "." + base64UrlEncode(sign(unsignedToken));
    }

    private Map<String, Object> parseClaims(String token) {
        if (!StringUtils.hasText(token)) {
            throw new JwtAuthenticationException("Token is empty.");
        }

        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new JwtAuthenticationException("Token format is invalid.");
        }

        String unsignedToken = parts[0] + "." + parts[1];
        String expectedSignature = base64UrlEncode(sign(unsignedToken));
        if (!expectedSignature.equals(parts[2])) {
            throw new JwtAuthenticationException("Token signature is invalid.");
        }

        try {
            byte[] payload = Base64.getUrlDecoder().decode(parts[1]);
            return objectMapper.readValue(payload, new TypeReference<>() {
            });
        } catch (IllegalArgumentException | IOException e) {
            throw new JwtAuthenticationException("Token payload is invalid.");
        }
    }

    private byte[] writeJson(Map<String, Object> value) {
        try {
            return objectMapper.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to write JWT JSON.", e);
        }
    }

    private byte[] sign(String value) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret, HMAC_ALGORITHM));
            return mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to sign JWT.", e);
        }
    }

    private String base64UrlEncode(byte[] value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
    }
}
