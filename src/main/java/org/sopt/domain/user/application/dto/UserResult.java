package org.sopt.domain.user.application.dto;

import java.time.LocalDateTime;

public record UserResult(
        Long id,
        String nickname,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
