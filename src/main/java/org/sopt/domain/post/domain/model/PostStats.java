package org.sopt.domain.post.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "post_stats")
/**
 * 게시글 반응 수 집계를 관리하는 1:1 통계 엔티티.
 * 과제 요구사항의 낙관적 락 재시도를 보여주기 위해 좋아요/스크랩 집계를 한 행에 모아 관리한다.
 */
public class PostStats {

    @Id
    private Long postId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private long likeCount;

    @Column(nullable = false)
    private long scrapCount;

    @Version
    private Long version;

    protected PostStats() {
    }

    /**
     * 게시글 통계 엔티티를 생성한다.
     *
     * @param post 대상 게시글
     */
    public PostStats(Post post) {
        this.post = post;
    }

    /**
     * 게시글 ID를 반환한다.
     *
     * @return 게시글 ID
     */
    public Long getPostId() {
        return postId;
    }

    /**
     * 연결된 게시글을 반환한다.
     *
     * @return 게시글
     */
    public Post getPost() {
        return post;
    }

    /**
     * 좋아요 수를 반환한다.
     *
     * @return 좋아요 수
     */
    public long getLikeCount() {
        return likeCount;
    }

    /**
     * 스크랩 수를 반환한다.
     *
     * @return 스크랩 수
     */
    public long getScrapCount() {
        return scrapCount;
    }

    /**
     * 반응 타입에 맞는 카운터를 증가시킨다.
     *
     * @param type 반응 타입
     */
    public void increaseReactionCount(ReactionType type) {
        switch (type) {
            case LIKE -> this.likeCount++;
            case SCRAP -> this.scrapCount++;
        }
    }

    /**
     * 반응 타입에 맞는 카운터를 감소시킨다.
     *
     * @param type 반응 타입
     */
    public void decreaseReactionCount(ReactionType type) {
        switch (type) {
            case LIKE -> decreaseLikeCount();
            case SCRAP -> decreaseScrapCount();
        }
    }

    /**
     * 좋아요 수를 0 미만으로 내려가지 않게 감소시킨다.
     */
    private void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    /**
     * 스크랩 수를 0 미만으로 내려가지 않게 감소시킨다.
     */
    private void decreaseScrapCount() {
        if (this.scrapCount > 0) {
            this.scrapCount--;
        }
    }
}
