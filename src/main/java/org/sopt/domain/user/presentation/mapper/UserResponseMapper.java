package org.sopt.domain.user.presentation.mapper;

import org.sopt.domain.user.application.dto.UserResult;
import org.sopt.domain.user.presentation.dto.response.UserResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 사용자 애플리케이션 결과를 API 응답 모델로 변환하는 매퍼.
 */
@Component
public class UserResponseMapper {

    /**
     * 단건 사용자 결과를 응답 모델로 변환한다.
     *
     * @param result 사용자 결과
     * @return 사용자 응답
     */
    public UserResponse toResponse(UserResult result) {
        return new UserResponse(
                result.id(),
                result.nickname(),
                result.createdAt(),
                result.updatedAt()
        );
    }

    /**
     * 사용자 결과 목록을 응답 목록으로 변환한다.
     *
     * @param results 사용자 결과 목록
     * @return 사용자 응답 목록
     */
    public List<UserResponse> toResponses(List<UserResult> results) {
        return results.stream()
                .map(this::toResponse)
                .toList();
    }
}
