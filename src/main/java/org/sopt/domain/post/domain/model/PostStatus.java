package org.sopt.domain.post.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 상태")
/**
 * 게시글 공개/운영 상태.
 */
public enum PostStatus {
    @Schema(description = "정상 공개 상태")
    PUBLISHED,
    @Schema(description = "일반 사용자에게 숨김 처리된 상태")
    HIDDEN,
    @Schema(description = "정책 위반 등으로 차단된 상태")
    BLOCKED,
    @Schema(description = "삭제된 상태")
    DELETED
}
