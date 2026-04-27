package org.sopt.domain.post.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.domain.model.PostStatus;

import java.time.LocalDateTime;

@Schema(description = "게시글 응답")
public record PostResponse(
        @Schema(description = "게시글 ID", example = "1")
        Long id,
        @Schema(description = "게시판 타입", example = "FREE")
        BoardType boardType,
        @Schema(description = "게시글 상태", example = "PUBLISHED")
        PostStatus status,
        @Schema(description = "상태 사유", example = "정상 게시글", nullable = true)
        String statusReason,
        @Schema(description = "게시글 제목", example = "점심 메뉴 추천 받습니다")
        String title,
        @Schema(description = "게시글 본문", example = "학교 근처에서 먹을만한 곳 있나요?")
        String content,
        @Schema(description = "익명 여부", example = "true")
        boolean isAnonymous,
        @Schema(description = "작성자 닉네임", example = "gyuill")
        String authorNickname,
        @Schema(description = "공감 수", example = "5")
        long likeCount,
        @Schema(description = "스크랩 수", example = "2")
        long scrapCount,
        @Schema(description = "생성 시각", example = "2026-04-27T16:00:00")
        LocalDateTime createdAt
) {
}
