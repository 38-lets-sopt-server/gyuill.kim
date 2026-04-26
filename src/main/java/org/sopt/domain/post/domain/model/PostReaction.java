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
 * 과제 필수 구현 범위는 아니지만 화면 설계서에 공감/스크랩이 포함되어 있어
 * 추후 기능 확장을 고려해 선제적으로 추가했습니다.
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

    public PostReaction(Post post, User user, ReactionType type) {
        this.post = post;
        this.user = user;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public User getUser() {
        return user;
    }

    public ReactionType getType() {
        return type;
    }
}
