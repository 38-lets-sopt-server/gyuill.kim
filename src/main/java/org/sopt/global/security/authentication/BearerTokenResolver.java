package org.sopt.global.security.authentication;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Authorization 헤더에서 Bearer 토큰을 추출한다.
 */
@Component
public class BearerTokenResolver {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * HTTP 요청에서 Bearer 토큰을 추출한다.
     *
     * @param request HTTP 요청
     * @return Bearer 토큰. 없으면 null
     */
    public String resolve(HttpServletRequest request) {
        return resolve(request.getHeader(AUTHORIZATION_HEADER));
    }

    /**
     * Authorization 헤더 값에서 Bearer 토큰을 추출한다.
     *
     * @param authorizationHeader Authorization 헤더 값
     * @return Bearer 토큰. 없으면 null
     */
    public String resolve(String authorizationHeader) {
        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            return null;
        }
        return authorizationHeader.substring(BEARER_PREFIX.length());
    }
}
