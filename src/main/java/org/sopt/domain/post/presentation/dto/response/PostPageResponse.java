package org.sopt.domain.post.presentation.dto.response;

import java.util.List;

public record PostPageResponse(
        List<PostResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext
) {
}
