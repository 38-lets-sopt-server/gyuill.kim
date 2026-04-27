package org.sopt.domain.user.application.dto;

/**
 * 사용자 수정에 필요한 애플리케이션 계층 입력 모델.
 */
public record UpdateUserCommand(String nickname) {
}
