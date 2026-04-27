package org.sopt.domain.post.application.service;

import org.sopt.domain.post.application.dto.PostCursorResult;
import org.sopt.domain.post.application.dto.PostResult;
import org.sopt.domain.post.domain.exception.PostNotAccessibleException;
import org.sopt.domain.post.domain.exception.PostNotFoundException;
import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.domain.model.Post;
import org.sopt.domain.post.domain.model.PostStatus;
import org.sopt.domain.post.domain.repository.PostRepository;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 게시글 읽기 전용 조회를 담당하는 query 서비스.
 * 공개 조회와 숨김 조회를 분리해 상태별 접근 정책을 명확히 드러낸다.
 */
@Service
@Transactional(readOnly = true)
public class PostQueryService {

    private final PostRepository postRepository;

    public PostQueryService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /**
     * 게시판 타입과 커서를 기준으로 공개 게시글 목록을 조회한다.
     *
     * @param boardType 게시판 타입, {@code null}이면 전체
     * @param cursor 다음 페이지 기준 커서
     * @param size 페이지 크기
     * @return 커서 기반 게시글 조회 결과
     */
    public PostCursorResult getPosts(BoardType boardType, Long cursor, int size) {
        Slice<Post> posts = postRepository.findAllByCursor(boardType, cursor, size);
        return toPostCursorResult(posts);
    }

    /**
     * 키워드로 공개 게시글을 검색한다.
     *
     * @param keyword 검색어
     * @param cursor 다음 페이지 기준 커서
     * @param size 페이지 크기
     * @return 커서 기반 검색 결과
     */
    public PostCursorResult searchPosts(String keyword, Long cursor, int size) {
        Slice<Post> posts = postRepository.search(keyword, cursor, size);
        return toPostCursorResult(posts);
    }

    /**
     * 일반 공개 규칙을 만족하는 게시글 상세를 조회한다.
     *
     * @param id 게시글 ID
     * @return 게시글 결과
     */
    public PostResult getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        if (!post.isVisibleToPublic()) {
            throw new PostNotAccessibleException(post);
        }
        return toPostResult(post);
    }

    /**
     * 숨김 게시글 확인용 상세 조회.
     * 숨김은 허용하되 삭제/차단 상태는 여전히 차단한다.
     * 일단은 과제 범위여서 이렇게 구현했지만 원래대로면 일반 사용자는 접근할 수 없어야 합니다.
     * 다만 아직 인증/인가가 없어서 임시 동작 확인용입니다.
     *
     * @param id 게시글 ID
     * @return 게시글 결과
     */
    public PostResult getHiddenPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        if (post.getStatus() == PostStatus.DELETED
                || post.getStatus() == PostStatus.BLOCKED) {
            throw new PostNotAccessibleException(post);
        }
        return toPostResult(post);
    }

    /**
     * Slice 조회 결과를 API 친화적인 커서 응답 모델로 변환한다.
     *
     * @param posts 게시글 Slice
     * @return 커서 결과
     */
    private PostCursorResult toPostCursorResult(Slice<Post> posts) {
        List<PostResult> content = posts.getContent().stream()
                .map(this::toPostResult)
                .toList();

        Long nextCursor = posts.hasNext() && !content.isEmpty()
                ? content.get(content.size() - 1).id()
                : null;
        return new PostCursorResult(content, nextCursor, posts.getSize(), posts.hasNext());
    }

    /**
     * 게시글 엔티티를 응답용 결과 모델로 변환한다.
     *
     * @param post 게시글 엔티티
     * @return 게시글 결과
     */
    private PostResult toPostResult(Post post) {
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
