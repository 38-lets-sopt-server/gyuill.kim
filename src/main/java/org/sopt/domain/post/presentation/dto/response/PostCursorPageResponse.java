package org.sopt.domain.post.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "게시글 커서 페이지 응답")
/**
 * 게시글 커서 페이지 응답 모델.
 */
public record PostCursorPageResponse(
        @Schema(description = "게시글 목록")
        List<PostResponse> content,
        @Schema(description = "다음 페이지 조회용 커서", example = "10", nullable = true)
        Long nextCursor,
        @Schema(description = "현재 페이지 크기", example = "10")
        int size,
        @Schema(description = "다음 페이지 존재 여부", example = "true")
        boolean hasNext
) {
}
