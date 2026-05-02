package org.sopt.domain.user.application.dto;

/**
 * 사용자 생성에 필요한 애플리케이션 계층 입력 모델 VO.
 */
public record CreateUserCommand(String nickname) {
}
