package org.sopt.domain.post.application.dto;

import org.sopt.domain.post.domain.model.BoardType;

public record CreatePostCommand(BoardType boardType, String title, String content, Long authorUserId, boolean isAnonymous) {
}
