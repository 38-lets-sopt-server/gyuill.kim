package org.sopt.domain.post.domain.exception;

import lombok.Getter;
import org.sopt.global.code.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Post 도메인에서 사용하는 오류 코드 모음.
 */
@Getter
public enum PostErrorCode implements ErrorCode {

    POST_NOT_FOUND("PST-E001", HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다."),
    POST_REACTION_CONFLICT("PST-E009", HttpStatus.CONFLICT, "게시글 반응 처리 중 충돌이 발생했습니다. 다시 시도해주세요."),
    POST_NOT_ACCESSIBLE("PST-E015", HttpStatus.NOT_FOUND, "접근할 수 없는 게시글입니다."),
    POST_NOT_UPDATABLE("PST-E016", HttpStatus.CONFLICT, "수정할 수 없는 게시글 상태입니다."),
    POST_NOT_REACTABLE("PST-E017", HttpStatus.CONFLICT, "반응할 수 없는 게시글 상태입니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    PostErrorCode(String code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
