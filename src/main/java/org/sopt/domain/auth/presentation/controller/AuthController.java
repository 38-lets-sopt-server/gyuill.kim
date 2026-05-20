package org.sopt.domain.auth.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.auth.application.dto.AuthTokenResult;
import org.sopt.domain.auth.application.service.AuthService;
import org.sopt.domain.auth.presentation.code.AuthSuccessCode;
import org.sopt.domain.auth.presentation.dto.request.GoogleOAuthLoginRequest;
import org.sopt.domain.auth.presentation.dto.request.LoginRequest;
import org.sopt.domain.auth.presentation.dto.request.TokenReissueRequest;
import org.sopt.domain.auth.presentation.dto.response.AuthTokenResponse;
import org.sopt.global.security.authentication.AuthenticatedUser;
import org.sopt.global.response.CommonApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;

    /**
     * 로그인 후 access token과 refresh token을 발급한다.
     *
     * @param request 로그인 요청
     * @return 토큰 응답
     */
    @PostMapping("/login")
    @Override
    public ResponseEntity<CommonApiResponse<AuthTokenResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        AuthTokenResult result = authService.login(request.loginId(), request.password());
        return CommonApiResponse.successResponse(AuthSuccessCode.LOGIN_SUCCESS, AuthTokenResponse.from(result));
    }

    /**
     * Google ID Token으로 로그인하거나 자동 회원가입 후 토큰을 발급한다.
     *
     * @param request Google OAuth 로그인 요청
     * @return 토큰 응답
     */
    @PostMapping("/oauth/google")
    @Override
    public ResponseEntity<CommonApiResponse<AuthTokenResponse>> loginWithGoogle(
            @Valid @RequestBody GoogleOAuthLoginRequest request
    ) {
        AuthTokenResult result = authService.loginWithGoogle(request.idToken());
        return CommonApiResponse.successResponse(AuthSuccessCode.LOGIN_SUCCESS, AuthTokenResponse.from(result));
    }

    /**
     * refresh token으로 새 토큰 쌍을 발급한다.
     *
     * @param request 토큰 재발급 요청
     * @return 새 토큰 응답
     */
    @PostMapping("/reissue")
    @Override
    public ResponseEntity<CommonApiResponse<AuthTokenResponse>> reissue(
            @Valid @RequestBody TokenReissueRequest request
    ) {
        AuthTokenResult result = authService.reissue(request.refreshToken());
        return CommonApiResponse.successResponse(AuthSuccessCode.TOKEN_REISSUED, AuthTokenResponse.from(result));
    }

    /**
     * refresh token을 삭제하고 현재 access token을 블랙리스트에 등록한다.
     *
     * @param authenticatedUser 인증된 사용자
     * @return 로그아웃 응답
     */
    @PostMapping("/logout")
    @Override
    public ResponseEntity<CommonApiResponse<Void>> logout(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        authService.logout(authenticatedUser);
        return CommonApiResponse.successResponse(AuthSuccessCode.LOGOUT_SUCCESS, null);
    }
}
