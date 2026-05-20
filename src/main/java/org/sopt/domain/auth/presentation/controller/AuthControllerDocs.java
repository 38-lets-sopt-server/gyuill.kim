package org.sopt.domain.auth.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sopt.domain.auth.domain.exception.AuthErrorCode;
import org.sopt.domain.auth.presentation.dto.request.LoginRequest;
import org.sopt.domain.auth.presentation.dto.request.TokenReissueRequest;
import org.sopt.domain.auth.presentation.dto.response.AuthTokenResponse;
import org.sopt.domain.user.domain.exception.UserErrorCode;
import org.sopt.global.annotation.ApiExceptions;
import org.sopt.global.code.GlobalErrorCode;
import org.sopt.global.response.CommonApiResponse;
import org.sopt.global.security.authentication.AuthenticatedUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "로그인, 로그아웃 및 토큰 재발급 API")
public interface AuthControllerDocs {

    @Operation(summary = "로그인", description = "로그인 ID와 비밀번호를 검증해 JWT를 발급합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    @ApiExceptions({UserErrorCode.class, GlobalErrorCode.class})
    ResponseEntity<CommonApiResponse<AuthTokenResponse>> login(
            @Valid @RequestBody LoginRequest request
    );

    @Operation(summary = "토큰 재발급", description = "refresh token을 검증해 새 JWT를 발급합니다.")
    @ApiResponse(responseCode = "200", description = "토큰 재발급 성공")
    @ApiExceptions({AuthErrorCode.class, UserErrorCode.class, GlobalErrorCode.class})
    ResponseEntity<CommonApiResponse<AuthTokenResponse>> reissue(
            @Valid @RequestBody TokenReissueRequest request
    );

    @Operation(summary = "로그아웃", description = "refresh token을 삭제하고 현재 access token을 블랙리스트에 등록합니다.")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    @ApiExceptions({AuthErrorCode.class, GlobalErrorCode.class})
    ResponseEntity<CommonApiResponse<Void>> logout(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    );
}
