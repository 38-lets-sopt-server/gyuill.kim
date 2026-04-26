package org.sopt.domain.post.presentation.dto.response;

import java.util.List;

public record PostCursorPageResponse(
        List<PostResponse> content,
        Long nextCursor,
        int size,
        boolean hasNext
) {
}
