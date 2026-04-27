package org.sopt.domain.post.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.sopt.domain.user.domain.model.User;
import org.sopt.global.entity.BaseTimeEntity;

/**
 * 게시글에 대한 사용자 반응을 저장하는 조인 엔티티.
 * 좋아요와 스크랩을 분리 엔티티로 두지 않고 type으로 일반화해 중복 구조를 줄였다.
 */
@Entity
@Table(
        name = "post_reactions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_post_reaction_post_user_type", columnNames = {"post_id", "user_id", "type"})
        }
)
public class PostReaction extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private ReactionType type;

    protected PostReaction() {
    }

    /**
     * 게시글 반응을 생성한다.
     *
     * @param post 대상 게시글
     * @param user 반응 사용자
     * @param type 반응 타입
     */
    public PostReaction(Post post, User user, ReactionType type) {
        this.post = post;
        this.user = user;
        this.type = type;
    }

    /**
     * 반응 ID를 반환한다.
     *
     * @return 반응 ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 대상 게시글을 반환한다.
     *
     * @return 게시글
     */
    public Post getPost() {
        return post;
    }

    /**
     * 반응 사용자를 반환한다.
     *
     * @return 사용자
     */
    public User getUser() {
        return user;
    }

    /**
     * 반응 타입을 반환한다.
     *
     * @return 반응 타입
     */
    public ReactionType getType() {
        return type;
    }
}
