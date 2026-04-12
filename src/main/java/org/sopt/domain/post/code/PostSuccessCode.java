package org.sopt.domain.post.code;

import org.sopt.global.code.SuccessCode;
import org.springframework.http.HttpStatus;

public enum PostSuccessCode implements SuccessCode {

    POST_CREATED(HttpStatus.CREATED, "게시글 등록 완료"),
    POST_READ(HttpStatus.OK, "게시글 조회 성공"),
    POST_LIST_READ(HttpStatus.OK, "게시글 목록 조회 성공"),
    POST_UPDATED(HttpStatus.OK, "게시글 수정 완료"),
    POST_DELETED(HttpStatus.OK, "게시글 삭제 완료"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    PostSuccessCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
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
