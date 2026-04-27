package org.sopt.domain.post.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 반응 토글 응답")
public record PostReactionToggleResponse(
        @Schema(description = "토글 후 반응 적용 여부", example = "true")
        boolean reacted
) {
}
