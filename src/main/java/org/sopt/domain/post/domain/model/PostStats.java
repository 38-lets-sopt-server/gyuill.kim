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

    public PostStats(Post post) {
        this.post = post;
    }

    public Long getPostId() {
        return postId;
    }

    public Post getPost() {
        return post;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public long getScrapCount() {
        return scrapCount;
    }

    public void increaseReactionCount(ReactionType type) {
        switch (type) {
            case LIKE -> this.likeCount++;
            case SCRAP -> this.scrapCount++;
        }
    }

    public void decreaseReactionCount(ReactionType type) {
        switch (type) {
            case LIKE -> decreaseLikeCount();
            case SCRAP -> decreaseScrapCount();
        }
    }

    private void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    private void decreaseScrapCount() {
        if (this.scrapCount > 0) {
            this.scrapCount--;
        }
    }
}
