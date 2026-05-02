package org.sopt.domain.post.domain.exception;

import org.sopt.domain.post.domain.model.Post;
import org.sopt.global.code.ErrorCode;
import org.sopt.global.exception.BaseException;

/**
 * 게시글 상태 때문에 발생하는 예외들의 공통 베이스 클래스.
 */
public abstract class PostStateException extends BaseException {

    /**
     * 상태 기반 예외를 생성한다.
     *
     * @param errorCode 에러 코드
     * @param post 상태 정보를 담을 게시글
     */
    protected PostStateException(ErrorCode errorCode, Post post) {
        super(errorCode, PostStateExceptionDetailsFactory.from(post));
    }
}
