package org.sopt.domain.post.domain.exception;

import org.sopt.domain.post.domain.model.Post;

/**
 * 게시글은 존재하지만 현재 상태상 조회를 허용할 수 없을 때 발생하는 예외.
 */
public class PostNotAccessibleException extends PostStateException {

    /**
     * 현재 게시글 상태 정보를 포함해 예외를 생성한다.
     *
     * @param post 접근 불가 게시글
     */
    public PostNotAccessibleException(Post post) {
        super(PostErrorCode.POST_NOT_ACCESSIBLE, post);
    }
}
