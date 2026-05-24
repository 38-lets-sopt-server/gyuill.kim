package org.sopt.domain.auth.presentation.mapper;

import org.sopt.domain.auth.domain.model.AuthTokenResult;
import org.sopt.domain.auth.presentation.dto.response.AuthTokenResponse;
import org.springframework.stereotype.Component;

/**
 * 인증 애플리케이션 결과를 API 응답 모델로 변환하는 매퍼.
 */
@Component
public class AuthTokenResponseMapper {

    /**
     * 토큰 발급 결과를 응답 모델로 변환한다.
     * refresh token은 HttpOnly 쿠키로 운반되므로 응답 본문에는 포함하지 않는다.
     *
     * @param result 토큰 발급 결과
     * @return 인증 토큰 응답
     */
    public AuthTokenResponse toResponse(AuthTokenResult result) {
        return new AuthTokenResponse(
                result.tokenType(),
                result.accessToken(),
                result.accessTokenExpiresAt()
        );
    }
}
