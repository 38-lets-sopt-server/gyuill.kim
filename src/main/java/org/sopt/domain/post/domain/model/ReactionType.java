package org.sopt.domain.post.domain.model;

/**
 * 게시글 반응 종류.
 * 같은 사용자가 같은 게시글에 서로 다른 반응을 동시에 가질 수 있도록 유니크 제약의 일부로 사용한다.
 */
public enum ReactionType {
    LIKE,
    SCRAP
}
