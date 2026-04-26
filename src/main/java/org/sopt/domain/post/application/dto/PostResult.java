package org.sopt.domain.post.application.dto;

import org.sopt.domain.post.domain.model.BoardType;

import java.time.LocalDateTime;

public record PostResult(
        Long id,
        BoardType boardType,
        String title,
        String content,
        String author,
        long likeCount,
        LocalDateTime createdAt
) {
}
