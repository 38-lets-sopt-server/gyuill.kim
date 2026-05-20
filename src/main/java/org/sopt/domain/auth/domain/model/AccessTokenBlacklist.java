package org.sopt.domain.auth.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.sopt.global.entity.BaseTimeEntity;

import java.time.LocalDateTime;

/**
 * 로그아웃된 access token의 jti를 저장하는 블랙리스트 엔티티.
 */
@Entity
@Table(name = "access_token_blacklists")
public class AccessTokenBlacklist extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 36)
    private String tokenId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    protected AccessTokenBlacklist() {
    }

    /**
     * 로그아웃된 access token 정보를 생성한다.
     *
     * @param tokenId JWT ID
     * @param userId 토큰 소유 사용자 ID
     * @param expiresAt access token 만료 시각
     */
    public AccessTokenBlacklist(String tokenId, Long userId, LocalDateTime expiresAt) {
        this.tokenId = tokenId;
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

}
