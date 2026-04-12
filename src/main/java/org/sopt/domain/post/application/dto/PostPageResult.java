package org.sopt.domain.post.application.dto;

import java.util.List;

public record PostPageResult(
        List<PostResult> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext
) {
}
