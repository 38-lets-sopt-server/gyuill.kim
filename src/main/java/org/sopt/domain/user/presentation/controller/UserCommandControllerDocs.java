package org.sopt.domain.user.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sopt.domain.user.domain.exception.UserErrorCode;
import org.sopt.domain.user.presentation.dto.request.CreateUserRequest;
import org.sopt.domain.user.presentation.dto.request.UpdateUserRequest;
import org.sopt.domain.user.presentation.dto.response.UserResponse;
import org.sopt.global.annotation.ApiExceptions;
import org.sopt.global.code.GlobalErrorCode;
import org.sopt.global.response.CommonApiResponse;
import org.sopt.global.security.AuthenticatedUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User", description = "사용자 생성, 수정, 삭제 API")
public interface UserCommandControllerDocs {

    @Operation(summary = "사용자 생성", description = "새 사용자를 생성합니다.")
    @ApiResponse(responseCode = "201", description = "사용자 생성 성공")
    @ApiExceptions({UserErrorCode.class, GlobalErrorCode.class})
    ResponseEntity<CommonApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request
    );

    @Operation(summary = "내 사용자 정보 수정", description = "인증된 사용자 본인의 닉네임을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 수정 성공")
    @ApiExceptions({UserErrorCode.class, GlobalErrorCode.class})
    ResponseEntity<CommonApiResponse<Void>> updateUser(
            @Valid @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    );

    @Operation(summary = "내 사용자 삭제", description = "인증된 사용자 본인을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "사용자 삭제 성공")
    @ApiExceptions({UserErrorCode.class, GlobalErrorCode.class})
    ResponseEntity<CommonApiResponse<Void>> deleteUser(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    );
}
