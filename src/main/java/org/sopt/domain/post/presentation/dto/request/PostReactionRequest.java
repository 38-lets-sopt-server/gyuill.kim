package org.sopt.domain.post.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.post.domain.exception.PostErrorCode;
import org.sopt.global.exception.BaseException;

@Schema(description = "게시글 반응 토글 요청")
public record PostReactionRequest(
        @Schema(description = "반응을 수행하는 사용자 ID", example = "1")
        Long userId
) {

    public void validate() {
        if (userId == null || userId < 1) {
            throw new BaseException(PostErrorCode.INVALID_REACTION_USER_ID);
        }
    }
}
