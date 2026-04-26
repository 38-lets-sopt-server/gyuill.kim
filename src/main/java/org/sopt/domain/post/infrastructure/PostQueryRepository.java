package org.sopt.domain.post.infrastructure;

import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.domain.model.Post;
import org.springframework.data.domain.Slice;

public interface PostQueryRepository {

    Slice<Post> findAllByCursor(BoardType boardType, Long cursor, int size);

    Slice<Post> search(String titleKeyword, String authorNickname, Long cursor, int size);
}
