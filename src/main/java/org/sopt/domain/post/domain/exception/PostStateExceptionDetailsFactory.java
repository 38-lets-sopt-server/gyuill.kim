package org.sopt.domain.post.domain.exception;

import org.sopt.domain.post.domain.model.Post;

import java.util.LinkedHashMap;
import java.util.Map;

public final class PostStateExceptionDetailsFactory {

    private PostStateExceptionDetailsFactory() {
    }

    public static Map<String, Object> from(Post post) {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("postId", post.getId());
        details.put("currentStatus", post.getStatus());
        details.put("statusReason", post.getStatusReason());
        return details;
    }
}
