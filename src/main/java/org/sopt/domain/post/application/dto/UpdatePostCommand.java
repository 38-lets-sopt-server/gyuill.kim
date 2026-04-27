package org.sopt.domain.post.application.dto;

/**
 * 게시글 수정에 필요한 애플리케이션 계층 입력 모델.
 */
public record UpdatePostCommand(String title, String content) {
}
