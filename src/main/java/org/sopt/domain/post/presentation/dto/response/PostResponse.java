package org.sopt.domain.post.presentation.dto.response;

import org.sopt.domain.post.application.dto.PostResult;
import org.sopt.domain.post.domain.model.BoardType;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        BoardType boardType,
        String title,
        String content,
        String author,
        LocalDateTime createdAt
) {
    public static PostResponse from(PostResult result) {
        return new PostResponse(
                result.id(),
                result.boardType(),
                result.title(),
                result.content(),
                result.author(),
                result.createdAt()
        );
    }
}
