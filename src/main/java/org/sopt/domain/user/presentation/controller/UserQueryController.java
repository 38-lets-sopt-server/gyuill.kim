package org.sopt.domain.user.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.domain.user.application.dto.UserResult;
import org.sopt.domain.user.application.service.UserQueryService;
import org.sopt.domain.user.domain.exception.UserErrorCode;
import org.sopt.domain.user.presentation.code.UserSuccessCode;
import org.sopt.domain.user.presentation.dto.response.UserResponse;
import org.sopt.domain.user.presentation.mapper.UserResponseMapper;
import org.sopt.global.annotation.ApiExceptions;
import org.sopt.global.code.GlobalErrorCode;
import org.sopt.global.response.CommonApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "사용자 조회 API")
public class UserQueryController {

    private final UserQueryService userQueryService;
    private final UserResponseMapper userResponseMapper;

    public UserQueryController(UserQueryService userQueryService, UserResponseMapper userResponseMapper) {
        this.userQueryService = userQueryService;
        this.userResponseMapper = userResponseMapper;
    }

    @GetMapping
    @Operation(summary = "사용자 목록 조회", description = "전체 사용자 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 목록 조회 성공")
    })
    @ApiExceptions({UserErrorCode.class, GlobalErrorCode.class})
    public ResponseEntity<CommonApiResponse<List<UserResponse>>> getUsers() {
        List<UserResponse> response = userResponseMapper.toResponses(userQueryService.getUsers());

        return CommonApiResponse.successResponse(UserSuccessCode.USER_LIST_READ, response);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "사용자 상세 조회", description = "사용자 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 상세 조회 성공")
    })
    @ApiExceptions({UserErrorCode.class, GlobalErrorCode.class})
    public ResponseEntity<CommonApiResponse<UserResponse>> getUser(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId
    ) {
        UserResult result = userQueryService.getUser(userId);
        UserResponse response = userResponseMapper.toResponse(result);

        return CommonApiResponse.successResponse(UserSuccessCode.USER_READ, response);
    }
}
