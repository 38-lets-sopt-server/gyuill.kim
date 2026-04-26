package org.sopt.domain.user.presentation.dto.response;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String nickname,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
