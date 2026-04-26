package org.sopt.domain.post.infrastructure;

import org.sopt.domain.post.domain.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostQueryRepository {

    Slice<Post> findAllByCursor(org.sopt.domain.post.domain.model.BoardType boardType, Long cursor, int size);

    Page<Post> search(String titleKeyword, String authorNickname, Pageable pageable);
}
