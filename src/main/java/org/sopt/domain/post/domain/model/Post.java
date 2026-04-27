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
import org.sopt.domain.post.domain.exception.PostNotReactableException;
import org.sopt.domain.post.domain.exception.PostNotUpdatableException;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PostStatus status;

    @Column(length = 255)
    private String statusReason;

    @Column(nullable = false)
    private boolean isAnonymous;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_user_id", nullable = false)
    private User authorUser;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private PostStats stats;

    protected Post() {
    }

    public Post(BoardType boardType, String title, String content, boolean anonymous, User authorUser) {
        this.boardType = boardType;
        this.title = title;
        this.content = content;
        this.status = PostStatus.PUBLISHED;
        this.isAnonymous = anonymous;
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

    public PostStatus getStatus() {
        return status;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public boolean isAnonymous() {
        return isAnonymous;
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

    public String getDisplayAuthorName() {
        if (isAnonymous) {
            return "익명";
        }
        return authorUser.isDeleted() ? "탈퇴한 사용자" : authorUser.getNickname();
    }

    public boolean isVisibleToPublic() {
        return status == PostStatus.PUBLISHED;
    }

    public boolean canUpdate() {
        return status == PostStatus.PUBLISHED || status == PostStatus.HIDDEN;
    }

    public boolean canReact() {
        return status == PostStatus.PUBLISHED;
    }

    public void update(String title, String content) {
        ensureUpdatable();
        this.title = title;
        this.content = content;
    }

    public void markDeleted() {
        if (status == PostStatus.DELETED) {
            return;
        }
        this.status = PostStatus.DELETED;
        this.statusReason = "사용자 삭제 요청";
    }

    public void markHidden(String reason) {
        if (status == PostStatus.DELETED) {
            throw new PostNotUpdatableException(this);
        }
        this.status = PostStatus.HIDDEN;
        this.statusReason = reason;
    }

    public void markBlocked(String reason) {
        // 관리자 차단 기능이 추가되면 이 전이를 실제 운영 플로우에서 사용한다.
        if (status == PostStatus.DELETED) {
            throw new PostNotUpdatableException(this);
        }
        this.status = PostStatus.BLOCKED;
        this.statusReason = reason;
    }

    public void ensureUpdatable() {
        if (!canUpdate()) {
            throw new PostNotUpdatableException(this);
        }
    }

    public void ensureReactable() {
        if (!canReact()) {
            throw new PostNotReactableException(this);
        }
    }

    public PostStats initializeStats() {
        if (this.stats == null) {
            this.stats = new PostStats(this);
        }
        return this.stats;
    }
}
