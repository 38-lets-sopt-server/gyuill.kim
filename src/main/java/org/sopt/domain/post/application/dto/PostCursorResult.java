package org.sopt.domain.post.application.dto;

import java.util.List;

public record PostCursorResult(
        List<PostResult> content,
        Long nextCursor,
        int size,
        boolean hasNext
) {
}
