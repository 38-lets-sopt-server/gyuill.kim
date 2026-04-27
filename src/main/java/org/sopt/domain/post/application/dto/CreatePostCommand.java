package org.sopt.domain.post.application.dto;

import org.sopt.domain.post.domain.model.BoardType;

/**
 * 게시글 생성에 필요한 애플리케이션 계층 입력 모델.
 */
public record CreatePostCommand(BoardType boardType, String title, String content, Long authorUserId, boolean isAnonymous) {
}
