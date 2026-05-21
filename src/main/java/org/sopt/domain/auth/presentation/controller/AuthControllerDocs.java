package org.sopt.domain.auth.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sopt.domain.auth.domain.exception.AuthErrorCode;
import org.sopt.domain.auth.presentation.cookie.RefreshTokenCookieFactory;
import org.sopt.domain.auth.presentation.dto.request.GoogleOAuthLoginRequest;
import org.sopt.domain.auth.presentation.dto.request.LoginRequest;
import org.sopt.domain.auth.presentation.dto.response.AuthTokenResponse;
import org.sopt.domain.user.domain.exception.UserErrorCode;
import org.sopt.global.annotation.ApiExceptions;
import org.sopt.global.code.GlobalErrorCode;
import org.sopt.global.response.CommonApiResponse;
import org.sopt.global.security.authentication.AuthenticatedUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "로그인, 로그아웃 및 토큰 재발급 API")
public interface AuthControllerDocs {

    @Operation(
            summary = "로그인",
            description = "로그인 ID와 비밀번호를 검증해 access token을 발급한다. refresh token은 HttpOnly 쿠키로 내려간다."
    )
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    @ApiExceptions({UserErrorCode.class, GlobalErrorCode.class})
    ResponseEntity<CommonApiResponse<AuthTokenResponse>> login(
            @Valid @RequestBody LoginRequest request
    );

    @Operation(
            summary = "Google OAuth 로그인",
            description = "Google ID Token을 검증해 로그인하거나 자동 회원가입 후 access token을 발급한다. refresh token은 HttpOnly 쿠키로 내려간다."
    )
    @ApiResponse(responseCode = "200", description = "Google OAuth 로그인 성공")
    @ApiExceptions({AuthErrorCode.class, GlobalErrorCode.class})
    ResponseEntity<CommonApiResponse<AuthTokenResponse>> loginWithGoogle(
            @Valid @RequestBody GoogleOAuthLoginRequest request
    );

    @Operation(
            summary = "토큰 재발급",
            description = "HttpOnly 쿠키로 전달된 refresh token을 검증해 새 access token을 발급하고 refresh token을 회전(rotation)한다."
    )
    @ApiResponse(responseCode = "200", description = "토큰 재발급 성공")
    @ApiExceptions({AuthErrorCode.class, UserErrorCode.class, GlobalErrorCode.class})
    ResponseEntity<CommonApiResponse<AuthTokenResponse>> reissue(
            @Parameter(
                    name = RefreshTokenCookieFactory.REFRESH_TOKEN_COOKIE_NAME,
                    description = "refresh token 쿠키",
                    in = ParameterIn.COOKIE,
                    required = true
            )
            @CookieValue(name = RefreshTokenCookieFactory.REFRESH_TOKEN_COOKIE_NAME) String refreshToken
    );

    @Operation(
            summary = "로그아웃",
            description = "refresh token을 삭제하고 현재 access token을 블랙리스트에 등록한 뒤 refresh token 쿠키를 만료시킨다."
    )
    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    @ApiExceptions({AuthErrorCode.class, GlobalErrorCode.class})
    ResponseEntity<CommonApiResponse<Void>> logout(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    );
}
