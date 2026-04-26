package org.sopt.domain.user.presentation.code;

import org.sopt.global.code.SuccessCode;
import org.springframework.http.HttpStatus;

public enum UserSuccessCode implements SuccessCode {

    USER_CREATED("USR-201", HttpStatus.CREATED, "사용자 등록 완료"),
    USER_READ("USR-200", HttpStatus.OK, "사용자 조회 성공"),
    USER_LIST_READ("USR-202", HttpStatus.OK, "사용자 목록 조회 성공"),
    USER_UPDATED("USR-203", HttpStatus.OK, "사용자 수정 완료"),
    USER_DELETED("USR-204", HttpStatus.OK, "사용자 삭제 완료"),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    UserSuccessCode(String code, HttpStatus httpStatus, String message) {
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
