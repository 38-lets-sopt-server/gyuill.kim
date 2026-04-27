package org.sopt.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
/**
 * 삭제 시각을 기록하는 소프트 삭제 베이스 엔티티.
 * 과제 범위에서는 복구 기능까지 구현하지 않고 조회 제외 기준으로만 사용하고있습니다.
 */
public abstract class SoftDeleteBaseEntity extends BaseTimeEntity {

    @Column
    private LocalDateTime deletedAt;

    /**
     * 삭제 시각을 반환한다.
     *
     * @return 삭제 시각, 삭제되지 않았다면 {@code null}
     */
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    /**
     * 현재 시각으로 삭제 처리한다.
     */
    public void markDeleted() {
        this.deletedAt = LocalDateTime.now();
    }
}
