package org.sopt.domain.post.application.dto;

import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.domain.model.Post;

import java.time.LocalDateTime;

public record PostResult(
        Long id,
        BoardType boardType,
        String title,
        String content,
        String author,
        LocalDateTime createdAt
) {
    public static PostResult from(Post post) {
        return new PostResult(
                post.getId(),
                post.getBoardType(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getCreatedAt()
        );
    }
}
