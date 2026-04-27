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

/**
 * PostReaction과 별도 테이블로 분리한 이유는 다음과 같은데요
 * - PostReaction은 "사용자-게시글-반응 타입" 원본 이력을 저장하는 조인 엔티티다.
 * - likeCount/scrapCount는 원본 반응을 빠르게 조회하기 위해 둔 집계 데이터다.
 * - 집계 필드를 PostReaction 쪽으로 밀어 넣거나 Post 본문 row와 강하게 결합하면
 *   조인 이력 저장 책임과 통계 갱신 책임이 섞여 도메인 의도가 흐려진다.
 * - 특히 과제 요구사항처럼 좋아요/스크랩 반응에 동시성이 몰리는 상황을 가정하면
 *   집계 row를 명시적으로 두고 @Version으로 보호하는 편이 충돌 지점을 설명하기 쉽다고 생각했습니다.
 *
 * 게시글 반응 수 집계를 관리하는 1:1 통계 엔티티.
 * 과제 요구사항의 낙관적 락 재시도를 보여주기 위해 좋아요/스크랩 집계를 한 행에 모아 관리합니다.
 *
 */
@Entity
@Table(name = "post_stats")
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
