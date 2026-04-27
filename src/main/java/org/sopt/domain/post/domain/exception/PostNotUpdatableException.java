package org.sopt.domain.post.domain.exception;

import org.sopt.domain.post.domain.model.Post;

public class PostNotUpdatableException extends PostStateException {

    public PostNotUpdatableException(Post post) {
        super(PostErrorCode.POST_NOT_UPDATABLE, post);
    }
}
