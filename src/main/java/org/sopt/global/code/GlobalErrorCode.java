package org.sopt.global.code;

import org.springframework.http.HttpStatus;

public enum GlobalErrorCode implements ErrorCode {

    INVALID_REQUEST("GLB-001", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    UNAUTHORIZED("GLB-002", HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN("GLB-003", HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    RESOURCE_NOT_FOUND("GLB-004", HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR("GLB-005", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    GlobalErrorCode(String code, HttpStatus httpStatus, String message) {
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
