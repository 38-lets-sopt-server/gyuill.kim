package org.sopt.domain.post.application.dto;

import java.util.List;

/**
 * 커서 기반 게시글 조회 결과 모델.
 */
public record PostCursorResult(
        List<PostResult> content,
        Long nextCursor,
        int size,
        boolean hasNext
) {
}
