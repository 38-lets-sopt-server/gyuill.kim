package org.sopt.domain.post.application.service.validator;

/**
 * 게시글 정책 검사 결과.
 */
public record PostModerationResult(boolean shouldHide, String reason) {

    /**
     * 게시글을 공개 상태로 둘 수 있는 결과를 생성한다.
     *
     * @return 공개 가능 결과
     */
    public static PostModerationResult published() {
        return new PostModerationResult(false, null);
    }

    /**
     * 게시글을 숨김 처리해야 하는 결과를 생성한다.
     *
     * @param reason 숨김 사유
     * @return 숨김 결과
     */
    public static PostModerationResult hidden(String reason) {
        return new PostModerationResult(true, reason);
    }
}
