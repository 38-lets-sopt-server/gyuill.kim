package org.sopt.domain.user.domain.exception;

import org.sopt.global.code.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * User 도메인에서 사용하는 오류 코드 모음.
 */
public enum UserErrorCode implements ErrorCode {

    USER_NOT_FOUND("USR-E001", HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    USER_LOGIN_ID_DUPLICATED("USR-E002", HttpStatus.BAD_REQUEST, "이미 가입된 유저입니다."),
    INVALID_LOGIN_CREDENTIALS("USR-E003", HttpStatus.UNAUTHORIZED, "로그인 정보가 올바르지 않습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    UserErrorCode(String code, HttpStatus httpStatus, String message) {
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
