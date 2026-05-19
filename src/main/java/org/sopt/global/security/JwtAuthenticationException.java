package org.sopt.global.security;

import org.springframework.security.core.AuthenticationException;

/**
 * JWT 인증 실패를 Spring Security 인증 예외로 전달한다.
 */
public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(String message) {
        super(message);
    }
}
