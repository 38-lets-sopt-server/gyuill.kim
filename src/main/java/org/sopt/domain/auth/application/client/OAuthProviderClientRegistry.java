package org.sopt.domain.auth.application.client;

import org.sopt.domain.auth.domain.model.OAuthProvider;
import org.sopt.domain.auth.domain.exception.AuthErrorCode;
import org.sopt.global.exception.BaseException;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * OAuth 제공자별 클라이언트를 조회한다.
 */
@Component
public class OAuthProviderClientRegistry {

    private final Map<OAuthProvider, OAuthProviderClient> clients;

    public OAuthProviderClientRegistry(List<OAuthProviderClient> clients) {
        this.clients = new EnumMap<>(OAuthProvider.class);
        clients.forEach(client -> this.clients.put(client.getProvider(), client));
    }

    /**
     * OAuth 제공자에 맞는 클라이언트를 반환한다.
     *
     * @param provider OAuth 제공자
     * @return OAuth 제공자 클라이언트
     */
    public OAuthProviderClient getClient(OAuthProvider provider) {
        OAuthProviderClient client = clients.get(provider);
        if (client == null) {
            throw new BaseException(AuthErrorCode.INVALID_OAUTH_TOKEN);
        }
        return client;
    }
}
