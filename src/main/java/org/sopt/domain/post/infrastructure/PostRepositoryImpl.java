package org.sopt.domain.post.infrastructure;

import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.domain.model.Post;
import org.sopt.domain.post.domain.repository.PostRepository;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
/**
 * Post 도메인 저장소를 JPA/Querydsl 구현체에 연결하는 어댑터.
 * 여기도 {@PostReactionRepositoryImpl.java}의 설명처럼 네이밍을 같이 고민했습니다.
 */
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository postJpaRepository;

    public PostRepositoryImpl(PostJpaRepository postJpaRepository) {
        this.postJpaRepository = postJpaRepository;
    }

    /**
     * 게시글을 저장한다.
     *
     * @param post 저장할 게시글
     * @return 저장된 게시글
     */
    @Override
    public Post save(Post post) {
        return postJpaRepository.save(post);
    }

    /**
     * 커서 기반 게시글 목록을 조회한다.
     *
     * @param boardType 게시판 타입
     * @param cursor 마지막 조회 게시글 ID 기준 커서
     * @param size 페이지 크기
     * @return 게시글 Slice
     */
    @Override
    public Slice<Post> findAllByCursor(BoardType boardType, Long cursor, int size) {
        return postJpaRepository.findAllByCursor(boardType, cursor, size);
    }

    /**
     * 키워드로 게시글을 검색한다.
     *
     * @param keyword 검색어
     * @param cursor 마지막 조회 게시글 ID 기준 커서
     * @param size 페이지 크기
     * @return 게시글 Slice
     */
    @Override
    public Slice<Post> search(String keyword, Long cursor, int size) {
        return postJpaRepository.search(keyword, cursor, size);
    }

    /**
     * 게시글을 단건 조회한다.
     *
     * @param id 게시글 ID
     * @return 게시글 조회 결과
     */
    @Override
    public Optional<Post> findById(Long id) {
        return postJpaRepository.findById(id);
    }
}
