package org.sopt.domain.post.domain.exception;

import org.sopt.domain.post.domain.model.Post;
public class PostNotAccessibleException extends PostStateException {

    public PostNotAccessibleException(Post post) {
        super(PostErrorCode.POST_NOT_ACCESSIBLE, post);
    }
}
