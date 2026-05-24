package org.sopt.domain.auth.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import org.sopt.domain.user.domain.model.User;
import org.sopt.global.entity.BaseTimeEntity;

/**
 * 외부 OAuth 계정과 내부 사용자를 연결한다.
 */
@Entity
@Table(
        name = "social_accounts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_social_account_provider_user",
                        columnNames = {"provider", "provider_user_id"}
                )
        }
)
public class SocialAccount extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OAuthProvider provider;

    @Column(name = "provider_user_id", nullable = false, length = 100)
    private String providerUserId;

    @Column(length = 255)
    private String email;

    @Column(length = 500)
    private String profileImageUrl;

    protected SocialAccount() {
    }

    /**
     * 소셜 계정 연결 정보를 생성한다.
     *
     * @param user 내부 사용자
     * @param provider OAuth 제공자
     * @param providerUserId OAuth 제공자의 사용자 식별자
     * @param email 이메일
     * @param profileImageUrl 프로필 이미지 URL
     */
    public SocialAccount(
            User user,
            OAuthProvider provider,
            String providerUserId,
            String email,
            String profileImageUrl
    ) {
        this.user = user;
        this.provider = provider;
        this.providerUserId = providerUserId;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    /**
     * 외부 프로필 정보를 최신 값으로 갱신한다.
     *
     * @param email 이메일
     * @param profileImageUrl 프로필 이미지 URL
     */
    public void updateProfile(String email, String profileImageUrl) {
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }
}
