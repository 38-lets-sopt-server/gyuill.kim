package org.sopt.domain.post.domain.exception;

import org.sopt.domain.post.domain.model.Post;

/**
 * 현재 게시글 상태에서는 반응을 받을 수 없을 때 발생하는 예외.
 */
public class PostNotReactableException extends PostStateException {

    /**
     * 현재 게시글 상태 정보를 포함해 예외를 생성한다.
     *
     * @param post 반응 불가 게시글
     */
    public PostNotReactableException(Post post) {
        super(PostErrorCode.POST_NOT_REACTABLE, post);
    }
}
