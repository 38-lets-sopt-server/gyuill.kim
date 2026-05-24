package org.sopt.domain.auth.domain.exception;

import lombok.Getter;
import org.sopt.global.code.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Auth 도메인에서 사용하는 오류 코드 모음.
 */
@Getter
public enum AuthErrorCode implements ErrorCode {

    INVALID_REFRESH_TOKEN("ATH-E001", HttpStatus.UNAUTHORIZED, "refresh token이 올바르지 않습니다."),
    REFRESH_TOKEN_REUSE_DETECTED("ATH-E005", HttpStatus.UNAUTHORIZED, "refresh token 재사용이 감지되어 세션이 무효화되었습니다."),
    INVALID_ACCESS_TOKEN("ATH-E002", HttpStatus.UNAUTHORIZED, "access token이 올바르지 않습니다."),
    INVALID_OAUTH_TOKEN("ATH-E003", HttpStatus.UNAUTHORIZED, "OAuth 토큰이 올바르지 않습니다."),
    OAUTH_CLIENT_NOT_CONFIGURED("ATH-E004", HttpStatus.INTERNAL_SERVER_ERROR, "OAuth 클라이언트 설정이 올바르지 않습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    AuthErrorCode(String code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
