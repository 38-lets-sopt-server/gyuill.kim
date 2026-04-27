package org.sopt.domain.post.application.dto;

import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.domain.model.PostStatus;

import java.time.LocalDateTime;

/**
 * 게시글 서비스가 프레젠테이션 계층에 전달하는 결과 모델.
 */
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
