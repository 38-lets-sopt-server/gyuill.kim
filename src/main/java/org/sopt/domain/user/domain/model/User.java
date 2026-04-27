package org.sopt.domain.user.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.sopt.global.entity.SoftDeleteBaseEntity;

/**
 * 게시글 작성 주체가 되는 사용자 엔티티.
 * 과제 범위에서는 인증 주체라기보다 게시글 작성자 식별과 표시 이름 제공 역할에 집중한다.
 */
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

    /**
     * 닉네임으로 사용자를 생성한다.
     *
     * @param nickname 사용자 닉네임
     */
    public User(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 사용자 식별자를 반환한다.
     *
     * @return 사용자 ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 현재 닉네임을 반환한다.
     *
     * @return 닉네임
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 닉네임을 변경한다.
     *
     * @param nickname 새 닉네임
     */
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 소프트 삭제 여부를 반환한다.
     *
     * @return 삭제되었다면 {@code true}
     */
    public boolean isDeleted() {
        return getDeletedAt() != null;
    }
}
