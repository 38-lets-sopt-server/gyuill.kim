package org.sopt.domain.post.application.dto;

import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.domain.model.PostStatus;

import java.time.LocalDateTime;

public record PostResult(
        Long id,
        BoardType boardType,
        PostStatus status,
        String statusReason,
        String title,
        String content,
        boolean isAnonymous,
        String authorNickname,
        long likeCount,
        long scrapCount,
        LocalDateTime createdAt
) {
}
