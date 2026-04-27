package org.sopt.domain.post.domain.exception;

import org.sopt.global.code.ErrorCode;
import org.springframework.http.HttpStatus;

public enum PostErrorCode implements ErrorCode {

    POST_NOT_FOUND("PST-E001", HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다."),
    INVALID_POST_TITLE("PST-E002", HttpStatus.BAD_REQUEST, "게시글 제목은 필수입니다."),
    INVALID_POST_CONTENT("PST-E003", HttpStatus.BAD_REQUEST, "게시글 내용은 필수입니다."),
    INVALID_BOARD_TYPE("PST-E004", HttpStatus.BAD_REQUEST, "게시판 종류는 필수입니다."),
    INVALID_PAGINATION("PST-E005", HttpStatus.BAD_REQUEST, "페이지 요청 값이 올바르지 않습니다."),
    INVALID_POST_AUTHOR("PST-E006", HttpStatus.BAD_REQUEST, "게시글 작성자 ID는 필수입니다."),
    INVALID_POST_TITLE_LENGTH("PST-E007", HttpStatus.BAD_REQUEST, "게시글 제목은 50자 이하여야 합니다."),
    INVALID_POST_CONTENT_LENGTH("PST-E008", HttpStatus.BAD_REQUEST, "게시글 내용은 10,000자 이하여야 합니다."),
    POST_REACTION_CONFLICT("PST-E009", HttpStatus.CONFLICT, "게시글 반응 처리 중 충돌이 발생했습니다. 다시 시도해주세요."),
    INVALID_POST_SEARCH_KEYWORD("PST-E010", HttpStatus.BAD_REQUEST, "검색어는 필수입니다."),
    INVALID_REACTION_USER_ID("PST-E011", HttpStatus.BAD_REQUEST, "게시글 반응 사용자 ID는 필수입니다."),
    INVALID_POST_SEARCH_KEYWORD_LENGTH("PST-E012", HttpStatus.BAD_REQUEST, "검색어는 100자 이하여야 합니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    PostErrorCode(String code, HttpStatus httpStatus, String message) {
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
