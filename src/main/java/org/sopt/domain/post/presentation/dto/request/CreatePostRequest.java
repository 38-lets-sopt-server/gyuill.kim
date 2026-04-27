package org.sopt.domain.post.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.post.domain.exception.PostErrorCode;
import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.global.exception.BaseException;

@Schema(description = "게시글 생성 요청")
/**
 * 게시글 생성 요청 본문.
 */
public record CreatePostRequest(
        @Schema(description = "게시판 타입", example = "FREE")
        BoardType boardType,
        @Schema(description = "게시글 제목", example = "점심 메뉴 추천 받습니다")
        String title,
        @Schema(description = "게시글 본문", example = "학교 근처에서 먹을만한 곳 있나요?")
        String content,
        @Schema(description = "작성자 사용자 ID", example = "1")
        Long authorUserId,
        @Schema(description = "익명 여부", example = "true")
        boolean isAnonymous
) {
    private static final int MAX_TITLE_LENGTH = 50;
    private static final int MAX_CONTENT_LENGTH = 10_000;

    /**
     * 과제 범위에서 필요한 기본 생성 입력값을 검증한다.
     *
     * @throws BaseException 입력값이 비어 있거나 길이 제한을 넘는 경우
     */
    public void validate() {
        // TODO: UpdatePostRequest와 검증 로직이 중복되지만 과제 범위에서는 유지하고 추후 validation annotation 도입 시 정리예정입니다.
        if (boardType == null) {
            throw new BaseException(PostErrorCode.INVALID_BOARD_TYPE);
        }
        if (title == null || title.isBlank()) {
            throw new BaseException(PostErrorCode.INVALID_POST_TITLE);
        }
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new BaseException(PostErrorCode.INVALID_POST_TITLE_LENGTH);
        }
        if (content == null || content.isBlank()) {
            throw new BaseException(PostErrorCode.INVALID_POST_CONTENT);
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new BaseException(PostErrorCode.INVALID_POST_CONTENT_LENGTH);
        }

        // TODO: 추후 인증/인가로 변경 예정, 현재로는 dto 단에서의 검증으로 대체
        if (authorUserId == null || authorUserId < 1) {
            throw new BaseException(PostErrorCode.INVALID_POST_AUTHOR);
        }
    }
}
