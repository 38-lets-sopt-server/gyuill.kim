package org.sopt.domain.user.presentation.mapper;

import org.sopt.domain.user.application.dto.UserResult;
import org.sopt.domain.user.presentation.dto.response.UserResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserResponseMapper {

    public UserResponse toResponse(UserResult result) {
        return new UserResponse(
                result.id(),
                result.nickname(),
                result.createdAt(),
                result.updatedAt()
        );
    }

    public List<UserResponse> toResponses(List<UserResult> results) {
        return results.stream()
                .map(this::toResponse)
                .toList();
    }
}
