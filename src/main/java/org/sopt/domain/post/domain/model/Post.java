package org.sopt.domain.post.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import org.sopt.domain.user.domain.model.User;
import org.sopt.global.entity.BaseTimeEntity;

@Entity
@Table(name = "posts")
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BoardType boardType;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_user_id", nullable = false)
    private User authorUser;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private PostStats stats;

    protected Post() {
    }

    public Post(BoardType boardType, String title, String content, User authorUser) {
        this.boardType = boardType;
        this.title = title;
        this.content = content;
        this.authorUser = authorUser;
        this.stats = new PostStats(this);
    }

    public Long getId() {
        return id;
    }

    public BoardType getBoardType() {
        return boardType;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public User getAuthorUser() {
        return authorUser;
    }

    public PostStats getStats() {
        return stats;
    }

    public long getLikeCount() {
        return stats == null ? 0 : stats.getLikeCount();
    }

    public long getScrapCount() {
        return stats == null ? 0 : stats.getScrapCount();
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostStats initializeStats() {
        if (this.stats == null) {
            this.stats = new PostStats(this);
        }
        return this.stats;
    }
}
