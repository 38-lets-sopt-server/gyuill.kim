package org.sopt.global.security;

/**
 * JWT 인증이 끝난 뒤 컨트롤러에서 사용할 인증 사용자 정보.
 */
public record AuthenticatedUser(Long userId) {
}
