package org.sopt.domain.auth.presentation.code;

import org.sopt.global.code.SuccessCode;
import org.springframework.http.HttpStatus;

/**
 * Auth API 성공 코드 모음.
 */
public enum AuthSuccessCode implements SuccessCode {

    LOGIN_SUCCESS("ATH-S001", HttpStatus.OK, "로그인 성공"),
    TOKEN_REISSUED("ATH-S002", HttpStatus.OK, "토큰 재발급 성공"),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    AuthSuccessCode(String code, HttpStatus httpStatus, String message) {
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
