package org.sopt.domain.post.application.dto;

public record UpdatePostCommand(String title, String content) {

    public static UpdatePostCommand of(String title, String content) {
        return new UpdatePostCommand(title, content);
    }
}
