package org.sopt.domain.auth.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * refresh token 원문을 DB에 저장하지 않기 위한 HMAC 해시 유틸리티.
 */
@Component
public class RefreshTokenHasher {

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private final byte[] secret;

    public RefreshTokenHasher(@Value("${refresh-token.hash-secret}") String secret) {
        if (!StringUtils.hasText(secret)) {
            throw new IllegalArgumentException("refresh token hash secret must not be blank.");
        }
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * refresh token 원문으로부터 저장용 HMAC 값을 만든다.
     *
     * @param refreshToken refresh token 원문
     * @return 저장용 HMAC 값
     */
    public String hash(String refreshToken) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret, HMAC_ALGORITHM));
            byte[] digest = mac.doFinal(refreshToken.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to hash refresh token.", e);
        }
    }

    /**
     * refresh token 원문과 저장된 HMAC 값을 constant-time 방식으로 비교한다.
     *
     * @param refreshToken refresh token 원문
     * @param expectedHash 저장된 HMAC 값
     * @return 일치 여부
     */
    public boolean matches(String refreshToken, String expectedHash) {
        if (!StringUtils.hasText(refreshToken) || !StringUtils.hasText(expectedHash)) {
            return false;
        }
        byte[] actual = hash(refreshToken).getBytes(StandardCharsets.US_ASCII);
        byte[] expected = expectedHash.getBytes(StandardCharsets.US_ASCII);
        return MessageDigest.isEqual(actual, expected);
    }
}
