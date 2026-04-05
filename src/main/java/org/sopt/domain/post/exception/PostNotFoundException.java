package org.sopt.domain.post.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException() {
        super("존재하지 않는 게시글입니다!");
    }
}
