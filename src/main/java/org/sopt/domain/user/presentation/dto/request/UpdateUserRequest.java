package org.sopt.domain.user.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.user.domain.exception.UserErrorCode;
import org.sopt.global.exception.BaseException;

@Schema(description = "사용자 수정 요청")
public record UpdateUserRequest(
        @Schema(description = "사용자 닉네임", example = "updated-gyuill")
        String nickname
) {
    private static final int MAX_NICKNAME_LENGTH = 30;

    public void validate() {
        if (nickname == null || nickname.isBlank()) {
            throw new BaseException(UserErrorCode.INVALID_USER_NICKNAME);
        }
        if (nickname.length() > MAX_NICKNAME_LENGTH) {
            throw new BaseException(UserErrorCode.INVALID_USER_NICKNAME_LENGTH);
        }
    }
}
