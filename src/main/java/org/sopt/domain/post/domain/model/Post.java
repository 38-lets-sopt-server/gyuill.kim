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

/**
 * 게시글 aggregate root.
 * 작성자 연관관계와 통계 엔티티를 함께 들고 있으며, 게시글 상태 전이 규칙도 이 엔티티가 책임진다.
 */
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

    /**
     * 새 게시글을 생성한다.
     *
     * @param boardType 게시판 타입
     * @param title 제목
     * @param content 본문
     * @param anonymous 익명 여부
     * @param authorUser 작성자
     */
    public Post(BoardType boardType, String title, String content, boolean anonymous, User authorUser) {
        this.boardType = boardType;
        this.title = title;
        this.content = content;
        this.status = PostStatus.PUBLISHED;
        this.isAnonymous = anonymous;
        this.authorUser = authorUser;
        this.stats = new PostStats(this);
    }

    /**
     * 게시글 ID를 반환한다.
     *
     * @return 게시글 ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 게시판 타입을 반환한다.
     *
     * @return 게시판 타입
     */
    public BoardType getBoardType() {
        return boardType;
    }

    /**
     * 제목을 반환한다.
     *
     * @return 제목
     */
    public String getTitle() {
        return title;
    }

    /**
     * 본문을 반환한다.
     *
     * @return 본문
     */
    public String getContent() {
        return content;
    }

    /**
     * 현재 게시글 상태를 반환한다.
     *
     * @return 게시글 상태
     */
    public PostStatus getStatus() {
        return status;
    }

    /**
     * 현재 상태 사유를 반환한다.
     *
     * @return 상태 사유
     */
    public String getStatusReason() {
        return statusReason;
    }

    /**
     * 익명 작성 여부를 반환한다.
     *
     * @return 익명 게시글이면 {@code true}
     */
    public boolean isAnonymous() {
        return isAnonymous;
    }

    /**
     * 작성자 엔티티를 반환한다.
     *
     * @return 작성자
     */
    public User getAuthorUser() {
        return authorUser;
    }

    /**
     * 반응 통계 엔티티를 반환한다.
     *
     * @return 통계 엔티티
     */
    public PostStats getStats() {
        return stats;
    }

    /**
     * 좋아요 수를 반환한다.
     *
     * @return 좋아요 수
     */
    public long getLikeCount() {
        return stats.getLikeCount();
    }

    /**
     * 스크랩 수를 반환한다.
     *
     * @return 스크랩 수
     */
    public long getScrapCount() {
        return stats.getScrapCount();
    }

    /**
     * 화면에 노출할 작성자 표시명을 반환한다.
     * 익명 게시글과 탈퇴 사용자를 별도 문자열 정책으로 처리한다.
     *
     * @return 표시용 작성자 이름
     */
    public String getDisplayAuthorName() {
        if (isAnonymous) {
            return "익명";
        }
        return authorUser.isDeleted() ? "탈퇴한 사용자" : authorUser.getNickname();
    }

    /**
     * 일반 사용자에게 공개 가능한 상태인지 판단한다.
     *
     * @return 공개 가능 여부
     */
    public boolean isVisibleToPublic() {
        return status == PostStatus.PUBLISHED;
    }

    /**
     * 현재 상태에서 수정 가능한지 판단한다.
     *
     * @return 수정 가능 여부
     */
    public boolean canUpdate() {
        return status == PostStatus.PUBLISHED || status == PostStatus.HIDDEN;
    }

    /**
     * 현재 상태에서 반응 가능한지 판단한다.
     *
     * @return 반응 가능 여부
     */
    public boolean canReact() {
        return status == PostStatus.PUBLISHED;
    }

    /**
     * 제목과 본문을 수정한다.
     *
     * @param title 새 제목
     * @param content 새 본문
     */
    public void update(String title, String content) {
        ensureUpdatable();
        this.title = title;
        this.content = content;
    }

    /**
     * 게시글을 삭제 상태로 전환한다.
     * 이미 삭제된 경우에는 멱등하게 무시한다.
     */
    public void markDeleted() {
        if (status == PostStatus.DELETED) {
            return;
        }
        this.status = PostStatus.DELETED;
        this.statusReason = "사용자 삭제 요청";
    }

    /**
     * 게시글을 숨김 상태로 전환한다.
     *
     * @param reason 숨김 사유
     */
    public void markHidden(String reason) {
        if (status == PostStatus.DELETED) {
            throw new PostNotUpdatableException(this);
        }
        this.status = PostStatus.HIDDEN;
        this.statusReason = reason;
    }

    /**
     * 게시글을 차단 상태로 전환한다.
     *
     * @param reason 차단 사유
     */
    public void markBlocked(String reason) {
        // 관리자 차단 기능이 추가되면 이 전이를 실제 운영 플로우에서 사용한다.
        if (status == PostStatus.DELETED) {
            throw new PostNotUpdatableException(this);
        }
        this.status = PostStatus.BLOCKED;
        this.statusReason = reason;
    }

    /**
     * 수정 가능 상태가 아니면 예외를 던진다.
     */
    public void ensureUpdatable() {
        if (!canUpdate()) {
            throw new PostNotUpdatableException(this);
        }
    }

    /**
     * 반응 가능 상태가 아니면 예외를 던진다.
     */
    public void ensureReactable() {
        if (!canReact()) {
            throw new PostNotReactableException(this);
        }
    }

}
