package org.sopt.domain.post.infrastructure;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.domain.model.Post;
import org.sopt.domain.post.domain.model.PostStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static org.sopt.domain.post.domain.model.QPost.post;
import static org.sopt.domain.post.domain.model.QPostStats.postStats;
import static org.sopt.domain.user.domain.model.QUser.user;

/**
 * Querydsl 기반 게시글 조회 구현체.
 * 현재 과제 규모에서는 작성자와 통계만 함께 로딩하면 되므로 fetch join으로 쿼리를 단순하게 유지합니다.
 */
@Repository
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final JPAQueryFactory queryFactory;

    public PostQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 공개 게시글 목록을 커서 기반으로 조회한다.
     *
     * @param boardType 게시판 타입
     * @param cursor 마지막 조회 게시글 ID 기준 커서
     * @param size 페이지 크기
     * @return 게시글 Slice
     */
    @Override
    public Slice<Post> findAllByCursor(BoardType boardType, Long cursor, int size) {
        List<Post> posts = queryFactory
                .selectFrom(post)
                .join(post.authorUser, user).fetchJoin()
                .leftJoin(post.stats, postStats).fetchJoin()
                .where(
                        publishedPost(),
                        boardTypeEq(boardType),
                        postIdLt(cursor)
                )
                .orderBy(post.id.desc())
                .limit(size + 1L)
                .fetch();

        boolean hasNext = posts.size() > size;
        List<Post> content = hasNext
                ? new ArrayList<>(posts.subList(0, size))
                : posts;
        return new SliceImpl<>(content, PageRequest.of(0, size), hasNext);
    }

    /**
     * 공개 게시글을 키워드로 검색한다.
     *
     * @param keyword 검색어
     * @param cursor 마지막 조회 게시글 ID 기준 커서
     * @param size 페이지 크기
     * @return 게시글 Slice
     */
    @Override
    public Slice<Post> search(String keyword, Long cursor, int size) {
        List<Post> posts = queryFactory
                .selectFrom(post)
                .join(post.authorUser, user).fetchJoin()
                .leftJoin(post.stats, postStats).fetchJoin()
                .where(
                        publishedPost(),
                        keywordContainedInTitleOrContent(keyword),
                        postIdLt(cursor)
                )
                .orderBy(post.id.desc())
                .limit(size + 1L)
                .fetch();

        boolean hasNext = posts.size() > size;
        List<Post> content = hasNext
                ? new ArrayList<>(posts.subList(0, size))
                : posts;
        return new SliceImpl<>(content, PageRequest.of(0, size), hasNext);
    }

    /**
     * 제목 또는 본문에 검색어가 포함되는지 조건식을 만든다.
     *
     * @param keyword 검색어
     * @return 조건식, 검색어가 비어 있으면 {@code null}
     */
    private BooleanExpression keywordContainedInTitleOrContent(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }
        return Expressions.anyOf(
                post.title.contains(keyword),
                post.content.contains(keyword)
        );
    }

    /**
     * 게시판 타입 필터 조건식을 만든다.
     *
     * @param boardType 게시판 타입
     * @return 조건식, 전체 조회면 {@code null}
     */
    private BooleanExpression boardTypeEq(BoardType boardType) {
        return boardType != null ? post.boardType.eq(boardType) : null;
    }

    /**
     * 공개 게시글만 조회하는 조건식을 만든다.
     *
     * @return 공개 상태 조건식
     */
    private BooleanExpression publishedPost() {
        return post.status.eq(PostStatus.PUBLISHED);
    }

    /**
     * 커서 이전 게시글만 조회하는 조건식을 만든다.
     *
     * @param cursor 마지막 조회 게시글 ID
     * @return 조건식, 첫 페이지면 {@code null}
     */
    private BooleanExpression postIdLt(Long cursor) {
        return cursor != null ? post.id.lt(cursor) : null;
    }
}
