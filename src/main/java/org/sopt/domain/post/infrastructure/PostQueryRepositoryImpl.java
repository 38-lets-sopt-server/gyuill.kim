package org.sopt.domain.post.infrastructure;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.sopt.domain.post.domain.model.Post;
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

@Repository
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final JPAQueryFactory queryFactory;

    public PostQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Slice<Post> findAllByCursor(org.sopt.domain.post.domain.model.BoardType boardType, Long cursor, int size) {
        List<Post> posts = queryFactory
                .selectFrom(post)
                .join(post.authorUser, user).fetchJoin()
                .leftJoin(post.stats, postStats).fetchJoin()
                .where(
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

    @Override
    public Slice<Post> search(String titleKeyword, String authorNickname, Long cursor, int size) {
        List<Post> posts = queryFactory
                .selectFrom(post)
                .join(post.authorUser, user).fetchJoin()
                .leftJoin(post.stats, postStats).fetchJoin()
                .where(
                        titleContains(titleKeyword),
                        authorNicknameContains(authorNickname),
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

    private BooleanExpression titleContains(String titleKeyword) {
        return StringUtils.hasText(titleKeyword) ? post.title.contains(titleKeyword) : null;
    }

    private BooleanExpression boardTypeEq(org.sopt.domain.post.domain.model.BoardType boardType) {
        return boardType != null ? post.boardType.eq(boardType) : null;
    }

    private BooleanExpression postIdLt(Long cursor) {
        return cursor != null ? post.id.lt(cursor) : null;
    }

    private BooleanExpression authorNicknameContains(String authorNickname) {
        return StringUtils.hasText(authorNickname) ? user.nickname.contains(authorNickname) : null;
    }
}
