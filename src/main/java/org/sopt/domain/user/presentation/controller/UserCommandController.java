package org.sopt.domain.user.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.domain.user.application.dto.CreateUserCommand;
import org.sopt.domain.user.application.dto.UpdateUserCommand;
import org.sopt.domain.user.application.dto.UserResult;
import org.sopt.domain.user.application.service.UserCommandService;
import org.sopt.domain.user.domain.exception.UserErrorCode;
import org.sopt.domain.user.presentation.code.UserSuccessCode;
import org.sopt.domain.user.presentation.dto.request.CreateUserRequest;
import org.sopt.domain.user.presentation.dto.request.UpdateUserRequest;
import org.sopt.domain.user.presentation.dto.response.UserResponse;
import org.sopt.domain.user.presentation.mapper.UserResponseMapper;
import org.sopt.global.annotation.ApiExceptions;
import org.sopt.global.code.GlobalErrorCode;
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
@Tag(name = "User Command", description = "사용자 생성, 수정, 삭제 API")
public class UserCommandController {

    private final UserCommandService userCommandService;
    private final UserResponseMapper userResponseMapper;

    public UserCommandController(UserCommandService userCommandService, UserResponseMapper userResponseMapper) {
        this.userCommandService = userCommandService;
        this.userResponseMapper = userResponseMapper;
    }

    @PostMapping
    @Operation(summary = "사용자 생성", description = "새 사용자를 생성합니다.")
    @ApiExceptions({UserErrorCode.class, GlobalErrorCode.class})
    public ResponseEntity<CommonApiResponse<UserResponse>> createUser(@RequestBody CreateUserRequest request) {
        request.validate();
        CreateUserCommand command = new CreateUserCommand(request.nickname());
        UserResult result = userCommandService.createUser(command);
        UserResponse response = userResponseMapper.toResponse(result);

        return CommonApiResponse.successResponse(UserSuccessCode.USER_CREATED, response);
    }

    @PatchMapping("/{userId}")
    @Operation(summary = "사용자 수정", description = "사용자 닉네임을 수정합니다.")
    @ApiExceptions({UserErrorCode.class, GlobalErrorCode.class})
    public ResponseEntity<CommonApiResponse<Void>> updateUser(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId,
            @RequestBody UpdateUserRequest request
    ) {
        request.validate();
        UpdateUserCommand command = new UpdateUserCommand(request.nickname());

        userCommandService.updateUser(userId, command);
        return CommonApiResponse.successResponse(UserSuccessCode.USER_UPDATED, null);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "사용자 삭제", description = "사용자를 삭제합니다.")
    @ApiExceptions({UserErrorCode.class, GlobalErrorCode.class})
    public ResponseEntity<CommonApiResponse<Void>> deleteUser(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId
    ) {
        userCommandService.deleteUser(userId);
        return CommonApiResponse.successResponse(UserSuccessCode.USER_DELETED, null);
    }
}
