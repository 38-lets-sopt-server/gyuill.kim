package org.sopt.domain.post.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.post.domain.exception.PostErrorCode;
import org.sopt.global.exception.BaseException;

@Schema(description = "게시글 반응 토글 요청")
/**
 * 게시글 반응 토글 요청 본문.
 */
public record PostReactionRequest(
        @Schema(description = "반응을 수행하는 사용자 ID", example = "1")
        Long userId
) {

    /**
     * 반응 주체 사용자 ID를 검증한다.
     *
     * @throws BaseException 사용자 ID가 비어 있거나 1 미만인 경우
     */
    public void validate() {
        if (userId == null || userId < 1) {
            throw new BaseException(PostErrorCode.INVALID_REACTION_USER_ID);
        }
    }
}
