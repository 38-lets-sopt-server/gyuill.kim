package org.sopt.domain.post.infrastructure;

import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.domain.model.Post;
import org.springframework.data.domain.Slice;

/**
 * 게시글 전용 커스텀 조회 저장소 추상화.
 */
public interface PostQueryRepository {

    /**
     * 커서 기반 게시글 목록을 조회한다.
     *
     * @param boardType 게시판 타입, {@code null}이면 전체
     * @param cursor 마지막 조회 게시글 ID 기준 커서
     * @param size 페이지 크기
     * @return 게시글 Slice
     */
    Slice<Post> findAllByCursor(BoardType boardType, Long cursor, int size);

    /**
     * 키워드로 커서 기반 게시글 검색을 수행한다.
     *
     * @param keyword 검색어
     * @param cursor 마지막 조회 게시글 ID 기준 커서
     * @param size 페이지 크기
     * @return 게시글 Slice
     */
    Slice<Post> search(String keyword, Long cursor, int size);
}
