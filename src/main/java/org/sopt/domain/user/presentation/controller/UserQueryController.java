package org.sopt.domain.user.presentation.controller;

import lombok.RequiredArgsConstructor;
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

/**
 * 사용자 읽기 API를 제공하는 컨트롤러.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserQueryController implements UserQueryControllerDocs {

    private final UserQueryService userQueryService;
    private final UserResponseMapper userResponseMapper;

    /**
     * 전체 사용자 목록을 조회한다.
     *
     * @return 사용자 목록 응답
     */
    @GetMapping
    @Override
    public ResponseEntity<CommonApiResponse<List<UserResponse>>> getUsers() {
        List<UserResponse> response = userResponseMapper.toResponses(userQueryService.getUsers());

        return CommonApiResponse.successResponse(UserSuccessCode.USER_LIST_READ, response);
    }

    /**
     * 사용자 상세 정보를 조회한다.
     *
     * @param userId 사용자 ID
     * @return 사용자 상세 응답
     */
    @GetMapping("/{userId}")
    @Override
    public ResponseEntity<CommonApiResponse<UserResponse>> getUser(
            @PathVariable Long userId
    ) {
        UserResult result = userQueryService.getUser(userId);
        UserResponse response = userResponseMapper.toResponse(result);

        return CommonApiResponse.successResponse(UserSuccessCode.USER_READ, response);
    }
}
