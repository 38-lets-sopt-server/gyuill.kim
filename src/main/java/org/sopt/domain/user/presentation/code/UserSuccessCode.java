package org.sopt.domain.user.presentation.code;

import org.sopt.global.code.SuccessCode;
import org.springframework.http.HttpStatus;

/**
 * User API 성공 코드 모음.
 */
public enum UserSuccessCode implements SuccessCode {

    USER_CREATED("USR-S001", HttpStatus.CREATED, "사용자 등록 완료"),
    USER_READ("USR-S002", HttpStatus.OK, "사용자 조회 성공"),
    USER_LIST_READ("USR-S003", HttpStatus.OK, "사용자 목록 조회 성공"),
    USER_UPDATED("USR-S004", HttpStatus.OK, "사용자 수정 완료"),
    USER_DELETED("USR-S005", HttpStatus.NO_CONTENT, "사용자 삭제 완료"),
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
