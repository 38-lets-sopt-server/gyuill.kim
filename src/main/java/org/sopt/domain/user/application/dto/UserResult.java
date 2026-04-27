package org.sopt.domain.user.application.dto;

import java.time.LocalDateTime;

/**
 * 사용자 조회/명령 처리 결과를 표현하는 애플리케이션 계층 반환 모델.
 */
public record UserResult(
        Long id,
        String nickname,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
