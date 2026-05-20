package org.sopt.domain.auth.domain.exception;

import org.sopt.global.code.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Auth 도메인에서 사용하는 오류 코드 모음.
 */
public enum AuthErrorCode implements ErrorCode {

    INVALID_REFRESH_TOKEN("ATH-E001", HttpStatus.UNAUTHORIZED, "refresh token이 올바르지 않습니다."),
    INVALID_ACCESS_TOKEN("ATH-E002", HttpStatus.UNAUTHORIZED, "access token이 올바르지 않습니다."),
    INVALID_OAUTH_TOKEN("ATH-E003", HttpStatus.UNAUTHORIZED, "OAuth 토큰이 올바르지 않습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    AuthErrorCode(String code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
