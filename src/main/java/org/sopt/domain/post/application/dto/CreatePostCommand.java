package org.sopt.domain.post.application.dto;

import org.sopt.domain.post.domain.model.BoardType;

public record CreatePostCommand(BoardType boardType, String title, String content, String author) {

    public static CreatePostCommand of(BoardType boardType, String title, String content, String author) {
        return new CreatePostCommand(boardType, title, content, author);
    }
}
