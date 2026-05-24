package org.sopt.domain.auth.infrastructure;

import org.sopt.domain.auth.domain.port.OAuthProviderClient;
import org.sopt.domain.auth.domain.port.OAuthUserProfile;
import org.sopt.domain.auth.domain.exception.AuthErrorCode;
import org.sopt.domain.auth.domain.model.OAuthProvider;
import org.sopt.global.exception.BaseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Google ID Token을 검증하고 Google 사용자 프로필을 반환한다.
 */
@Component
public class GoogleOAuthProviderClient implements OAuthProviderClient {

    private static final String GOOGLE_ISSUER = "https://accounts.google.com";
    private static final String GOOGLE_JWK_SET_URI = "https://www.googleapis.com/oauth2/v3/certs";
    private static final String EMAIL_CLAIM = "email";
    private static final String EMAIL_VERIFIED_CLAIM = "email_verified";
    private static final String NAME_CLAIM = "name";
    private static final String PICTURE_CLAIM = "picture";
    private static final Duration CLOCK_SKEW = Duration.ofSeconds(60);

    private final JwtDecoder jwtDecoder;
    private final List<String> clientIds;

    public GoogleOAuthProviderClient(
            @Value("${oauth.google.client-ids:}") String clientIds
    ) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(GOOGLE_JWK_SET_URI).build();
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(
                new JwtTimestampValidator(CLOCK_SKEW),
                new JwtIssuerValidator(GOOGLE_ISSUER)
        );
        decoder.setJwtValidator(validator);
        this.jwtDecoder = decoder;
        this.clientIds = parseClientIds(clientIds);
    }

    /**
     * 지원하는 OAuth 제공자를 반환한다.
     *
     * @return Google
     */
    @Override
    public OAuthProvider getProvider() {
        return OAuthProvider.GOOGLE;
    }

    /**
     * Google ID Token을 검증하고 사용자 프로필을 반환한다.
     *
     * @param token Google ID Token
     * @return 검증된 Google 사용자 프로필
     */
    @Override
    public OAuthUserProfile verify(String token) {
        if (clientIds.isEmpty()) {
            throw new BaseException(AuthErrorCode.OAUTH_CLIENT_NOT_CONFIGURED);
        }

        try {
            Jwt jwt = jwtDecoder.decode(token);
            if (jwt.getAudience().stream().noneMatch(clientIds::contains)) {
                throw new BaseException(AuthErrorCode.INVALID_OAUTH_TOKEN);
            }
            if (!Boolean.TRUE.equals(jwt.getClaimAsBoolean(EMAIL_VERIFIED_CLAIM))) {
                throw new BaseException(AuthErrorCode.INVALID_OAUTH_TOKEN);
            }

            String providerUserId = jwt.getSubject();
            String email = jwt.getClaimAsString(EMAIL_CLAIM);
            String nickname = resolveNickname(jwt.getClaimAsString(NAME_CLAIM), email);
            String profileImageUrl = jwt.getClaimAsString(PICTURE_CLAIM);

            if (!StringUtils.hasText(providerUserId)) {
                throw new BaseException(AuthErrorCode.INVALID_OAUTH_TOKEN);
            }

            return new OAuthUserProfile(
                    OAuthProvider.GOOGLE,
                    providerUserId,
                    email,
                    nickname,
                    profileImageUrl
            );
        } catch (BaseException e) {
            throw e;
        } catch (JwtException | IllegalArgumentException e) {
            throw new BaseException(AuthErrorCode.INVALID_OAUTH_TOKEN);
        }
    }

    private List<String> parseClientIds(String clientIds) {
        if (!StringUtils.hasText(clientIds)) {
            return List.of();
        }
        return Arrays.stream(clientIds.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .toList();
    }

    private String resolveNickname(String name, String email) {
        if (StringUtils.hasText(name)) {
            return truncate(name, 30);
        }
        if (StringUtils.hasText(email)) {
            int atIndex = email.indexOf("@");
            String localPart = atIndex > 0 ? email.substring(0, atIndex) : email;
            return truncate(localPart.toLowerCase(Locale.ROOT), 30);
        }
        return "google-user";
    }

    private String truncate(String value, int maxLength) {
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }
}
