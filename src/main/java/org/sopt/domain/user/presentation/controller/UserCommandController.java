package org.sopt.domain.user.presentation.controller;

import org.sopt.domain.user.application.dto.CreateUserCommand;
import org.sopt.domain.user.application.dto.UpdateUserCommand;
import org.sopt.domain.user.application.dto.UserResult;
import org.sopt.domain.user.application.service.UserCommandService;
import org.sopt.domain.user.presentation.code.UserSuccessCode;
import org.sopt.domain.user.presentation.dto.request.CreateUserRequest;
import org.sopt.domain.user.presentation.dto.request.UpdateUserRequest;
import org.sopt.domain.user.presentation.dto.response.UserResponse;
import org.sopt.domain.user.presentation.mapper.UserResponseMapper;
import org.sopt.global.response.CommonApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserCommandController {

    private final UserCommandService userCommandService;
    private final UserResponseMapper userResponseMapper;

    public UserCommandController(UserCommandService userCommandService, UserResponseMapper userResponseMapper) {
        this.userCommandService = userCommandService;
        this.userResponseMapper = userResponseMapper;
    }

    @PostMapping
    public ResponseEntity<CommonApiResponse<UserResponse>> createUser(@RequestBody CreateUserRequest request) {
        request.validate();
        CreateUserCommand command = new CreateUserCommand(request.nickname());
        UserResult result = userCommandService.createUser(command);
        UserResponse response = userResponseMapper.toResponse(result);

        return CommonApiResponse.successResponse(UserSuccessCode.USER_CREATED, response);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<CommonApiResponse<Void>> updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequest request) {
        request.validate();
        UpdateUserCommand command = new UpdateUserCommand(request.nickname());

        userCommandService.updateUser(userId, command);
        return CommonApiResponse.successResponse(UserSuccessCode.USER_UPDATED, null);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<CommonApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        userCommandService.deleteUser(userId);
        return CommonApiResponse.successResponse(UserSuccessCode.USER_DELETED, null);
    }
}
