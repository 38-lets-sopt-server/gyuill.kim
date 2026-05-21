package org.sopt.domain.auth.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.auth.application.dto.AuthTokenResult;
import org.sopt.domain.auth.application.service.AuthService;
import org.sopt.domain.auth.presentation.code.AuthSuccessCode;
import org.sopt.domain.auth.presentation.cookie.RefreshTokenCookieFactory;
import org.sopt.domain.auth.presentation.dto.request.GoogleOAuthLoginRequest;
import org.sopt.domain.auth.presentation.dto.request.LoginRequest;
import org.sopt.domain.auth.presentation.dto.response.AuthTokenResponse;
import org.sopt.domain.auth.presentation.mapper.AuthTokenResponseMapper;
import org.sopt.global.security.authentication.AuthenticatedUser;
import org.sopt.global.response.CommonApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 API를 제공하는 컨트롤러.
 * access token은 응답 본문으로, refresh token은 HttpOnly 쿠키로 운반한다.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;
    private final AuthTokenResponseMapper authTokenResponseMapper;
    private final RefreshTokenCookieFactory refreshTokenCookieFactory;

    /**
     * 로그인 후 access token을 응답 본문으로, refresh token을 HttpOnly 쿠키로 발급한다.
     *
     * @param request 로그인 요청
     * @return 토큰 응답 (refresh token은 Set-Cookie 헤더로 전달)
     */
    @PostMapping("/login")
    @Override
    public ResponseEntity<CommonApiResponse<AuthTokenResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        AuthTokenResult result = authService.login(request.loginId(), request.password());
        return tokenResponse(AuthSuccessCode.LOGIN_SUCCESS, result);
    }

    /**
     * Google ID Token으로 로그인하거나 자동 회원가입 후 토큰을 발급한다.
     *
     * @param request Google OAuth 로그인 요청
     * @return 토큰 응답 (refresh token은 Set-Cookie 헤더로 전달)
     */
    @PostMapping("/oauth/google")
    @Override
    public ResponseEntity<CommonApiResponse<AuthTokenResponse>> loginWithGoogle(
            @Valid @RequestBody GoogleOAuthLoginRequest request
    ) {
        AuthTokenResult result = authService.loginWithGoogle(request.idToken());
        return tokenResponse(AuthSuccessCode.LOGIN_SUCCESS, result);
    }

    /**
     * HttpOnly 쿠키로 전달된 refresh token을 검증해 새 토큰 쌍을 발급하고 refresh token을 회전한다.
     *
     * @param refreshToken 쿠키로 전달된 refresh token
     * @return 새 토큰 응답 (새 refresh token은 Set-Cookie 헤더로 전달)
     */
    @PostMapping("/reissue")
    @Override
    public ResponseEntity<CommonApiResponse<AuthTokenResponse>> reissue(
            @CookieValue(name = RefreshTokenCookieFactory.REFRESH_TOKEN_COOKIE_NAME) String refreshToken
    ) {
        AuthTokenResult result = authService.reissue(refreshToken);
        return tokenResponse(AuthSuccessCode.TOKEN_REISSUED, result);
    }

    /**
     * refresh token을 삭제하고 현재 access token을 블랙리스트에 등록한 뒤 쿠키를 만료시킨다.
     *
     * @param authenticatedUser 인증된 사용자
     * @return 로그아웃 응답 (만료된 refresh token 쿠키를 Set-Cookie로 전달)
     */
    @PostMapping("/logout")
    @Override
    public ResponseEntity<CommonApiResponse<Void>> logout(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        authService.logout(authenticatedUser);
        ResponseCookie expired = refreshTokenCookieFactory.expire();
        return ResponseEntity
                .status(AuthSuccessCode.LOGOUT_SUCCESS.getHttpStatus())
                .header(HttpHeaders.SET_COOKIE, expired.toString())
                .body(CommonApiResponse.successBody(AuthSuccessCode.LOGOUT_SUCCESS, null));
    }

    private ResponseEntity<CommonApiResponse<AuthTokenResponse>> tokenResponse(
            AuthSuccessCode successCode,
            AuthTokenResult result
    ) {
        AuthTokenResponse response = authTokenResponseMapper.toResponse(result);
        ResponseCookie refreshCookie = refreshTokenCookieFactory.create(result.refreshToken());
        return ResponseEntity
                .status(successCode.getHttpStatus())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(CommonApiResponse.successBody(successCode, response));
    }
}
