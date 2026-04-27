package org.sopt.domain.post.presentation.mapper;

import org.sopt.domain.post.application.dto.PostCursorResult;
import org.sopt.domain.post.application.dto.PostResult;
import org.sopt.domain.post.presentation.dto.response.PostCursorPageResponse;
import org.sopt.domain.post.presentation.dto.response.PostResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostResponseMapper {

    public PostResponse toResponse(PostResult result) {
        return new PostResponse(
                result.id(),
                result.boardType(),
                result.title(),
                result.content(),
                result.isAnonymous(),
                result.authorNickname(),
                result.likeCount(),
                result.scrapCount(),
                result.createdAt()
        );
    }

    public List<PostResponse> toResponses(List<PostResult> results) {
        return results.stream()
                .map(this::toResponse)
                .toList();
    }

    public PostCursorPageResponse toCursorPageResponse(PostCursorResult result) {
        return new PostCursorPageResponse(
                toResponses(result.content()),
                result.nextCursor(),
                result.size(),
                result.hasNext()
        );
    }
}
