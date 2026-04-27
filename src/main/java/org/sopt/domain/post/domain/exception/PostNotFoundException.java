package org.sopt.domain.post.domain.exception;

import org.sopt.global.exception.BaseException;

import java.util.Map;

/**
 * 게시글을 찾을 수 없을 때 발생하는 예외.
 */
public class PostNotFoundException extends BaseException {

    /**
     * 상세 정보에 조회 대상 게시글 ID를 담아 예외를 생성한다.
     *
     * @param postId 조회에 실패한 게시글 ID
     */
    public PostNotFoundException(Long postId) {
        super(PostErrorCode.POST_NOT_FOUND, Map.of("postId", postId));
    }
}
