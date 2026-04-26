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
     * 과제 요구사항의 좋아요 동시성 제어를 위해 Post 집계 필드를 낙관적 락 대상으로 둔다.
     * 반응 엔티티는 insert/delete 중심이라 실제 충돌 감지는 집계 루트인 Post에 두는 편이 더 명확하다.
     */
    @Column(nullable = false)
    private long likeCount;

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
    public User getAuthorUser() { return authorUser; }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
}
