package org.sopt.domain.post.domain.exception;

import org.sopt.domain.post.domain.model.Post;

public class PostNotReactableException extends PostStateException {

    public PostNotReactableException(Post post) {
        super(PostErrorCode.POST_NOT_REACTABLE, post);
    }
}
