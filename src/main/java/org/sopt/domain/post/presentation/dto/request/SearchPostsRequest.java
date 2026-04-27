package org.sopt.domain.post.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.post.domain.exception.PostErrorCode;
import org.sopt.global.exception.BaseException;
import org.springframework.util.StringUtils;

@Schema(description = "게시글 검색 요청")
/**
 * 게시글 검색 요청 파라미터 모델.
 */
public record SearchPostsRequest(
        @Schema(description = "검색 키워드", example = "스프링")
        String keyword,
        @Schema(description = "다음 페이지 조회용 커서", example = "10", nullable = true)
        Long cursor,
        @Schema(description = "페이지 크기", example = "10")
        int size
) {
    private static final int MAX_PAGE_SIZE = 100;
    private static final int MAX_KEYWORD_LENGTH = 100;

    /**
     * 검색어와 페이징 요청값을 검증한다.
     *
     * @throws BaseException 검색어가 비어 있거나 길이 제한을 넘는 경우
     */
    public void validate() {
        if (!StringUtils.hasText(keyword)) {
            throw new BaseException(PostErrorCode.INVALID_POST_SEARCH_KEYWORD);
        }
        if (cursor != null && cursor < 1) {
            throw new BaseException(PostErrorCode.INVALID_PAGINATION);
        }
        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new BaseException(PostErrorCode.INVALID_PAGINATION);
        }
        if (keyword.length() > MAX_KEYWORD_LENGTH) {
            throw new BaseException(PostErrorCode.INVALID_POST_SEARCH_KEYWORD_LENGTH);
        }
    }
}
