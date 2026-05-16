package org.sopt.domain.post.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 게시글 검색 요청 파라미터 모델.
 */
@Schema(description = "게시글 검색 요청")
public record SearchPostsRequest(
        @Schema(description = "검색 키워드", example = "스프링")
        @NotBlank(message = "검색어는 필수입니다.")
        @Size(max = 100, message = "검색어는 100자 이하여야 합니다.")
        String keyword,

        @Schema(description = "다음 페이지 조회용 커서", example = "10", nullable = true)
        @Min(value = 1, message = "페이지 요청 값이 올바르지 않습니다.")
        Long cursor,

        @Schema(description = "페이지 크기", example = "10")
        @Min(value = 1, message = "페이지 요청 값이 올바르지 않습니다.")
        @Max(value = 100, message = "페이지 요청 값이 올바르지 않습니다.")
        Integer size
) {
    public SearchPostsRequest {
        if (size == null) {
            size = 10;
        }
    }
}
