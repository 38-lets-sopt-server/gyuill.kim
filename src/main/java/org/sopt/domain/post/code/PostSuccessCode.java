package org.sopt.domain.post.code;

import org.sopt.global.code.SuccessCode;
import org.springframework.http.HttpStatus;

public enum PostSuccessCode implements SuccessCode {

    POST_CREATED("PST-201", HttpStatus.CREATED, "게시글 등록 완료"),
    POST_READ("PST-200", HttpStatus.OK, "게시글 조회 성공"),
    POST_LIST_READ("PST-202", HttpStatus.OK, "게시글 목록 조회 성공"),
    POST_UPDATED("PST-203", HttpStatus.OK, "게시글 수정 완료"),
    POST_DELETED("PST-204", HttpStatus.OK, "게시글 삭제 완료"),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    PostSuccessCode(String code, HttpStatus httpStatus, String message) {
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
