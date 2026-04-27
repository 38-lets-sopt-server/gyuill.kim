package org.sopt.domain.post.application.service.validator;

public record PostModerationResult(boolean shouldHide, String reason) {

    public static PostModerationResult published() {
        return new PostModerationResult(false, null);
    }

    public static PostModerationResult hidden(String reason) {
        return new PostModerationResult(true, reason);
    }
}
