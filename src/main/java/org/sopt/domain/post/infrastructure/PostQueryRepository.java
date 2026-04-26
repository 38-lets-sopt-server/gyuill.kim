package org.sopt.domain.post.infrastructure;

import org.sopt.domain.post.domain.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostQueryRepository {

    Page<Post> search(String titleKeyword, String authorNickname, Pageable pageable);
}
