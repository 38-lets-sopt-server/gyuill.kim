package org.sopt.domain.user.domain.exception;

import org.sopt.global.code.ErrorCode;
import org.springframework.http.HttpStatus;

public enum UserErrorCode implements ErrorCode {

    USER_NOT_FOUND("USR-E001", HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    INVALID_USER_NICKNAME("USR-E002", HttpStatus.BAD_REQUEST, "사용자 닉네임은 필수입니다."),
    INVALID_USER_NICKNAME_LENGTH("USR-E003", HttpStatus.BAD_REQUEST, "사용자 닉네임은 30자 이하여야 합니다."),
    USER_HAS_POSTS("USR-E004", HttpStatus.BAD_REQUEST, "작성한 게시글이 있는 사용자는 삭제할 수 없습니다."),
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
