package org.sopt.domain.post.dto.response;

public class CreatePostResponse {
    Long id;
    public String message;

    public CreatePostResponse(Long id, String message) {
        this.id = id;
        this.message = message;
    }
}
