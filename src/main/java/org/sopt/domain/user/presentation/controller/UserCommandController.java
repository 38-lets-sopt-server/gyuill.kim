package org.sopt.domain.user.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import org.sopt.global.security.AuthenticatedUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자 쓰기 API를 제공하는 컨트롤러.
 * 서비스 계층의 command/query 분리 의도를 HTTP 계층에도 그대로 반영한다.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserCommandController implements UserCommandControllerDocs {

    private final UserCommandService userCommandService;
    private final UserResponseMapper userResponseMapper;

    /**
     * 사용자를 생성한다.
     *
     * @param request 사용자 생성 요청
     * @return 생성된 사용자 응답
     */
    @PostMapping
    @Override
    public ResponseEntity<CommonApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request
    ) {
        CreateUserCommand command = new CreateUserCommand(request.loginId(), request.nickname(), request.password());
        UserResult result = userCommandService.createUser(command);
        UserResponse response = userResponseMapper.toResponse(result);

        return CommonApiResponse.successResponse(UserSuccessCode.USER_CREATED, response);
    }

    /**
     * 인증된 사용자 본인의 닉네임을 수정한다.
     *
     * @param request 사용자 수정 요청
     * @param authenticatedUser 인증 사용자
     * @return 공통 성공 응답
     */
    @PatchMapping("/me")
    @Override
    public ResponseEntity<CommonApiResponse<Void>> updateUser(
            @Valid @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        UpdateUserCommand command = new UpdateUserCommand(request.nickname());

        userCommandService.updateUser(authenticatedUser.userId(), command);
        return CommonApiResponse.successResponse(UserSuccessCode.USER_UPDATED, null);
    }

    /**
     * 인증된 사용자 본인을 삭제한다.
     *
     * @param authenticatedUser 인증 사용자
     * @return 공통 성공 응답
     */
    @DeleteMapping("/me")
    @Override
    public ResponseEntity<CommonApiResponse<Void>> deleteUser(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        userCommandService.deleteUser(authenticatedUser.userId());
        return CommonApiResponse.successResponse(UserSuccessCode.USER_DELETED, null);
    }
}
