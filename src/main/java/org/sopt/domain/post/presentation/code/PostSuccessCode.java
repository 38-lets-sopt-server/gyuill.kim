package org.sopt.domain.post.presentation.code;

import org.sopt.global.code.SuccessCode;
import org.springframework.http.HttpStatus;

/**
 * Post API 성공 코드 모음.
 */
public enum PostSuccessCode implements SuccessCode {

    POST_CREATED("PST-S001", HttpStatus.CREATED, "게시글 등록 완료"),
    POST_READ("PST-S002", HttpStatus.OK, "게시글 조회 성공"),
    POST_LIST_READ("PST-S003", HttpStatus.OK, "게시글 목록 조회 성공"),
    POST_UPDATED("PST-S004", HttpStatus.OK, "게시글 수정 완료"),
    POST_DELETED("PST-S005", HttpStatus.NO_CONTENT, "게시글 삭제 완료"),
    POST_LIKE_TOGGLED("PST-S006", HttpStatus.OK, "게시글 좋아요 토글 완료"),
    POST_SCRAP_TOGGLED("PST-S007", HttpStatus.OK, "게시글 스크랩 토글 완료"),
    POST_HIDDEN_READ("PST-S008", HttpStatus.OK, "숨김 게시글 조회 성공"),
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
