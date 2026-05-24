package org.sopt.domain.post.application.mapper;

import org.sopt.domain.post.application.dto.PostResult;
import org.sopt.domain.post.domain.model.Post;

/**
 * 게시글 엔티티를 애플리케이션 결과 모델로 변환하는 유틸 매퍼.
 */
public final class PostResultMapper {

    private PostResultMapper() {
    }

    /**
     * 게시글 엔티티를 결과 모델로 변환한다.
     *
     * @param post 게시글 엔티티
     * @return 게시글 결과
     */
    public static PostResult toResult(Post post) {
        return new PostResult(
                post.getId(),
                post.getBoardType(),
                post.getStatus(),
                post.getStatusReason(),
                post.getTitle(),
                post.getContent(),
                post.isAnonymous(),
                post.getDisplayAuthorName(),
                post.getLikeCount(),
                post.getScrapCount(),
                post.getCreatedAt()
        );
    }
}
