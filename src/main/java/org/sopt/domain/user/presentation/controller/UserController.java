package org.sopt.domain.user.presentation.controller;

import org.sopt.domain.user.application.UserCommandService;
import org.sopt.domain.user.application.UserQueryService;
import org.sopt.domain.user.application.dto.CreateUserCommand;
import org.sopt.domain.user.application.dto.UpdateUserCommand;
import org.sopt.domain.user.application.dto.UserResult;
import org.sopt.domain.user.presentation.code.UserSuccessCode;
import org.sopt.domain.user.presentation.dto.request.CreateUserRequest;
import org.sopt.domain.user.presentation.dto.request.UpdateUserRequest;
import org.sopt.domain.user.presentation.dto.response.UserResponse;
import org.sopt.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public UserController(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody CreateUserRequest request) {
        request.validate();
        UserResult result = userCommandService.createUser(new CreateUserCommand(request.nickname()));
        return ApiResponse.success(UserSuccessCode.USER_CREATED, new UserResponse(
                result.id(),
                result.nickname(),
                result.createdAt(),
                result.updatedAt()
        ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsers() {
        List<UserResponse> responses = userQueryService.getUsers().stream()
                .map(result -> new UserResponse(
                        result.id(),
                        result.nickname(),
                        result.createdAt(),
                        result.updatedAt()
                ))
                .toList();
        return ApiResponse.success(UserSuccessCode.USER_LIST_READ, responses);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long userId) {
        UserResult result = userQueryService.getUser(userId);
        return ApiResponse.success(UserSuccessCode.USER_READ, new UserResponse(
                result.id(),
                result.nickname(),
                result.createdAt(),
                result.updatedAt()
        ));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequest request) {
        request.validate();
        userCommandService.updateUser(userId, new UpdateUserCommand(request.nickname()));
        return ApiResponse.success(UserSuccessCode.USER_UPDATED, null);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        userCommandService.deleteUser(userId);
        return ApiResponse.success(UserSuccessCode.USER_DELETED, null);
    }
}
