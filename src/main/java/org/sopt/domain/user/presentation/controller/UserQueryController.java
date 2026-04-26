package org.sopt.domain.user.presentation.controller;

import org.sopt.domain.user.application.dto.UserResult;
import org.sopt.domain.user.application.service.UserQueryService;
import org.sopt.domain.user.presentation.code.UserSuccessCode;
import org.sopt.domain.user.presentation.dto.response.UserResponse;
import org.sopt.domain.user.presentation.mapper.UserResponseMapper;
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
    private final UserResponseMapper userResponseMapper;

    public UserQueryController(UserQueryService userQueryService, UserResponseMapper userResponseMapper) {
        this.userQueryService = userQueryService;
        this.userResponseMapper = userResponseMapper;
    }

    @GetMapping
    public ResponseEntity<CommonApiResponse<List<UserResponse>>> getUsers() {
        List<UserResponse> response = userResponseMapper.toResponses(userQueryService.getUsers());

        return CommonApiResponse.successResponse(UserSuccessCode.USER_LIST_READ, response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CommonApiResponse<UserResponse>> getUser(@PathVariable Long userId) {
        UserResult result = userQueryService.getUser(userId);
        UserResponse response = userResponseMapper.toResponse(result);

        return CommonApiResponse.successResponse(UserSuccessCode.USER_READ, response);
    }
}
