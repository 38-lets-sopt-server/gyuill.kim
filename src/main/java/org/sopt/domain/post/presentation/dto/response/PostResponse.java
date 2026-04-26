package org.sopt.domain.post.presentation.dto.response;

import org.sopt.domain.post.domain.model.BoardType;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        BoardType boardType,
        String title,
        String content,
        String author,
        long likeCount,
        long scrapCount,
        LocalDateTime createdAt
) {
}
