package org.sopt.domain.auth.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.auth.application.dto.AuthTokenResult;
import org.sopt.domain.auth.application.service.AuthService;
import org.sopt.domain.auth.presentation.code.AuthSuccessCode;
import org.sopt.domain.auth.presentation.dto.request.LoginRequest;
import org.sopt.domain.auth.presentation.dto.request.TokenReissueRequest;
import org.sopt.domain.auth.presentation.dto.response.AuthTokenResponse;
import org.sopt.global.response.CommonApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 API를 제공하는 컨트롤러.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "로그인 및 토큰 재발급 API")
public class AuthController {

    private final AuthService authService;

    /**
     * 로그인 후 access token과 refresh token을 발급한다.
     *
     * @param request 로그인 요청
     * @return 토큰 응답
     */
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 ID와 비밀번호를 검증해 JWT를 발급합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    public ResponseEntity<CommonApiResponse<AuthTokenResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        AuthTokenResult result = authService.login(request.loginId(), request.password());
        return CommonApiResponse.successResponse(AuthSuccessCode.LOGIN_SUCCESS, AuthTokenResponse.from(result));
    }

    /**
     * refresh token으로 새 토큰 쌍을 발급한다.
     *
     * @param request 토큰 재발급 요청
     * @return 새 토큰 응답
     */
    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급", description = "refresh token을 검증해 새 JWT를 발급합니다.")
    @ApiResponse(responseCode = "200", description = "토큰 재발급 성공")
    public ResponseEntity<CommonApiResponse<AuthTokenResponse>> reissue(
            @Valid @RequestBody TokenReissueRequest request
    ) {
        AuthTokenResult result = authService.reissue(request.refreshToken());
        return CommonApiResponse.successResponse(AuthSuccessCode.TOKEN_REISSUED, AuthTokenResponse.from(result));
    }
}
