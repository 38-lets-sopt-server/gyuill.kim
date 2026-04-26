package org.sopt.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class SoftDeleteBaseEntity extends BaseTimeEntity {

    @Column
    private LocalDateTime deletedAt;

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void markDeleted() {
        this.deletedAt = LocalDateTime.now();
    }
}
