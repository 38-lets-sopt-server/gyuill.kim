package org.sopt.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 생성 시각과 수정 시각을 공통 관리하는 베이스 엔티티.
 * JPAAudit + 어노테이션의 조합으로 객체 생성, 변동 시 자동으로 필드를 채워줍니다.
 * 이것도 지금처럼 요구사항이 단순할 때는 사용할 때 문제가 없는데
 * 각 데이터의 보관 정책이 복잡해질수록 관리 및 설계 난이도가 올라가는 것 같아요.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 엔티티 생성 시각을 반환한다.
     *
     * @return 생성 시각
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 엔티티 최종 수정 시각을 반환한다.
     *
     * @return 수정 시각
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
