package org.sopt.domain.post.domain.exception;

import org.sopt.domain.post.domain.model.Post;
import org.sopt.global.exception.ExceptionDetails;

import java.util.Map;

/**
 * 게시글 상태 예외에 공통으로 들어갈 상세 정보 생성기.
 */
public final class PostStateExceptionDetailsFactory {

    private PostStateExceptionDetailsFactory() {
    }

    /**
     * 게시글 상태를 구조화된 상세 정보 맵으로 변환한다.
     *
     * @param post 게시글 엔티티
     * @return 상세 정보 맵
     */
    public static Map<String, Object> from(Post post) {
        return ExceptionDetails.builder()
                .put("postId", post.getId())
                .put("currentStatus", post.getStatus())
                .put("statusReason", post.getStatusReason())
                .build();
    }
}
