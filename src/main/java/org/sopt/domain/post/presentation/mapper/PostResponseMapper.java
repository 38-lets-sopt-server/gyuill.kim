package org.sopt.domain.post.presentation.mapper;

import org.sopt.domain.post.application.dto.PostCursorResult;
import org.sopt.domain.post.application.dto.PostResult;
import org.sopt.domain.post.presentation.dto.response.PostCursorPageResponse;
import org.sopt.domain.post.presentation.dto.response.PostResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 게시글 애플리케이션 결과를 API 응답 모델로 변환하는 매퍼.
 * 컨트롤러가 너무 지저분해져서 따로 빼게 됐습니다.
 */
@Component
public class PostResponseMapper {

    /**
     * 게시글 단건 결과를 응답 모델로 변환한다.
     *
     * @param result 게시글 결과
     * @return 게시글 응답
     */
    public PostResponse toResponse(PostResult result) {
        return new PostResponse(
                result.id(),
                result.boardType(),
                result.status(),
                result.statusReason(),
                result.title(),
                result.content(),
                result.isAnonymous(),
                result.authorNickname(),
                result.likeCount(),
                result.scrapCount(),
                result.createdAt()
        );
    }

    /**
     * 게시글 결과 목록을 응답 목록으로 변환한다.
     *
     * @param results 게시글 결과 목록
     * @return 게시글 응답 목록
     */
    public List<PostResponse> toResponses(List<PostResult> results) {
        return results.stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * 커서 조회 결과를 페이지 응답으로 변환한다.
     *
     * @param result 커서 결과
     * @return 페이지 응답
     */
    public PostCursorPageResponse toCursorPageResponse(PostCursorResult result) {
        return new PostCursorPageResponse(
                toResponses(result.content()),
                result.nextCursor(),
                result.size(),
                result.hasNext()
        );
    }
}
