package org.sopt.domain.post.infrastructure;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.sopt.domain.post.domain.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.sopt.domain.post.domain.model.QPost.post;
import static org.sopt.domain.user.domain.model.QUser.user;

@Repository
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final JPAQueryFactory queryFactory;

    public PostQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Post> search(String titleKeyword, String authorNickname, Pageable pageable) {
        List<Post> content = queryFactory
                .selectFrom(post)
                .join(post.authorUser, user).fetchJoin()
                .where(
                        titleContains(titleKeyword),
                        authorNicknameContains(authorNickname)
                )
                .orderBy(post.createdAt.desc(), post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = countQuery(titleKeyword, authorNickname).fetchOne();
        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    private JPAQuery<Long> countQuery(String titleKeyword, String authorNickname) {
        return queryFactory
                .select(post.count())
                .from(post)
                .join(post.authorUser, user)
                .where(
                        titleContains(titleKeyword),
                        authorNicknameContains(authorNickname)
                );
    }

    private BooleanExpression titleContains(String titleKeyword) {
        return StringUtils.hasText(titleKeyword) ? post.title.contains(titleKeyword) : null;
    }

    private BooleanExpression authorNicknameContains(String authorNickname) {
        return StringUtils.hasText(authorNickname) ? user.nickname.contains(authorNickname) : null;
    }
}
