package org.sopt.domain.post.domain.repository;

import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.domain.model.Post;
import org.springframework.data.domain.Slice;

import java.util.Optional;

/**
 * Post aggregate가 의존하는 저장소 추상화.
 */
public interface PostRepository {

    /**
     * 게시글을 저장한다.
     *
     * @param post 저장할 게시글
     * @return 저장된 게시글
     */
    Post save(Post post);

    /**
     * 커서 기반으로 게시글 목록을 조회한다.
     *
     * @param boardType 게시판 타입, {@code null}이면 전체
     * @param cursor 마지막 조회 게시글 ID 기준 커서
     * @param size 페이지 크기
     * @return 게시글 Slice
     */
    Slice<Post> findAllByCursor(BoardType boardType, Long cursor, int size);

    /**
     * 키워드로 게시글을 검색한다.
     *
     * @param keyword 검색어
     * @param cursor 마지막 조회 게시글 ID 기준 커서
     * @param size 페이지 크기
     * @return 게시글 Slice
     */
    Slice<Post> search(String keyword, Long cursor, int size);

    /**
     * 게시글을 ID로 조회한다.
     *
     * @param id 게시글 ID
     * @return 게시글 조회 결과
     */
    Optional<Post> findById(Long id);
}
