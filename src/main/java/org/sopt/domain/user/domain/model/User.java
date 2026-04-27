package org.sopt.domain.user.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.sopt.global.entity.SoftDeleteBaseEntity;

@Entity
@Table(name = "users")
public class User extends SoftDeleteBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String nickname;

    protected User() {
    }

    public User(String nickname) {
        this.nickname = nickname;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isDeleted() {
        return getDeletedAt() != null;
    }
}
