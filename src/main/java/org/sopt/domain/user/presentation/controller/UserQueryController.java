package org.sopt.domain.user.presentation.controller;

import org.sopt.domain.user.application.dto.UserResult;
import org.sopt.domain.user.application.service.UserQueryService;
import org.sopt.domain.user.presentation.code.UserSuccessCode;
import org.sopt.domain.user.presentation.dto.response.UserResponse;
import org.sopt.global.response.CommonApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserQueryController {

    private final UserQueryService userQueryService;

    public UserQueryController(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @GetMapping
    public ResponseEntity<CommonApiResponse<List<UserResponse>>> getUsers() {
        List<UserResponse> responses = userQueryService.getUsers().stream()
                .map(result -> new UserResponse(
                        result.id(),
                        result.nickname(),
                        result.createdAt(),
                        result.updatedAt()
                ))
                .toList();
        return CommonApiResponse.success(UserSuccessCode.USER_LIST_READ, responses);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CommonApiResponse<UserResponse>> getUser(@PathVariable Long userId) {
        UserResult result = userQueryService.getUser(userId);
        return CommonApiResponse.success(UserSuccessCode.USER_READ, new UserResponse(
                result.id(),
                result.nickname(),
                result.createdAt(),
                result.updatedAt()
        ));
    }
}
