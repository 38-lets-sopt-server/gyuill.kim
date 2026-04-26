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
import jakarta.persistence.Table;
import jakarta.persistence.Version;
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

    /**
     * 반응 집계 필드는 Post 집계 루트에 중복 저장하고 충돌 감지는 @Version으로 일괄 처리한다.
     * 반응 엔티티 자체는 insert/delete 중심이라 충돌 감지는 집계 루트가 더 명확하다.
     */
    @Column(nullable = false)
    private long likeCount;

    @Column(nullable = false)
    private long scrapCount;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_user_id", nullable = false)
    private User authorUser;

    protected Post() {
    }

    public Post(BoardType boardType, String title, String content, User authorUser) {
        this.boardType = boardType;
        this.title = title;
        this.content = content;
        this.authorUser = authorUser;
    }

    public Long getId() { return id; }
    public BoardType getBoardType() { return boardType; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public long getLikeCount() { return likeCount; }
    public long getScrapCount() { return scrapCount; }
    public User getAuthorUser() { return authorUser; }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void increaseReactionCount(ReactionType type) {
        switch (type) {
            case LIKE -> this.likeCount++;
            case SCRAP -> this.scrapCount++;
        }
    }

    public void decreaseReactionCount(ReactionType type) {
        switch (type) {
            case LIKE -> {
                if (this.likeCount > 0) this.likeCount--;
            }
            case SCRAP -> {
                if (this.scrapCount > 0) this.scrapCount--;
            }
        }
    }
}
