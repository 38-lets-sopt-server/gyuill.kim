package org.sopt.domain.auth.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.sopt.domain.user.domain.model.User;
import org.sopt.global.entity.BaseTimeEntity;

import java.time.LocalDateTime;

/**
 * 사용자별 refresh token 저장 엔티티.
 * 재발급 시 JWT 자체 검증에 더해 서버에 저장된 토큰과 일치하는지 확인한다.
 */
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, unique = true, length = 500)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    protected RefreshToken() {
    }

    /**
     * refresh token 저장 데이터를 생성한다.
     *
     * @param user 토큰 소유 사용자
     * @param token refresh token
     * @param expiresAt 만료 시각
     */
    public RefreshToken(User user, String token, LocalDateTime expiresAt) {
        this.user = user;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    /**
     * refresh token ID를 반환한다.
     *
     * @return refresh token ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 토큰 소유 사용자를 반환한다.
     *
     * @return 사용자
     */
    public User getUser() {
        return user;
    }

    /**
     * 저장된 refresh token을 반환한다.
     *
     * @return refresh token
     */
    public String getToken() {
        return token;
    }

    /**
     * 만료 시각을 반환한다.
     *
     * @return 만료 시각
     */
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    /**
     * 새 refresh token으로 교체한다.
     *
     * @param token 새 refresh token
     * @param expiresAt 새 만료 시각
     */
    public void updateToken(String token, LocalDateTime expiresAt) {
        this.token = token;
        this.expiresAt = expiresAt;
    }

    /**
     * 전달된 토큰이 저장된 토큰과 같은지 확인한다.
     *
     * @param token 비교할 refresh token
     * @return 일치 여부
     */
    public boolean matches(String token) {
        return this.token.equals(token);
    }

    /**
     * 저장된 토큰이 만료됐는지 확인한다.
     *
     * @return 만료 여부
     */
    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
    }
}
