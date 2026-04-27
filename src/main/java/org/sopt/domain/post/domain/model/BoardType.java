package org.sopt.domain.post.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시판 유형")
/**
 * 게시글이 속하는 게시판 종류.
 */
public enum BoardType {
    @Schema(description = "자유게시판")
    FREE,
    @Schema(description = "인기 게시판")
    HOT,
    @Schema(description = "비밀 게시판")
    SECRET
}
