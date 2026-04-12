package org.sopt.domain.post.application.dto;

public record CreatePostCommand(String title, String content, String author) {

    public static CreatePostCommand of(String title, String content, String author) {
        return new CreatePostCommand(title, content, author);
    }
}
