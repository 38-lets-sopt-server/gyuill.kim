package org.sopt.domain.auth.infrastructure;

import org.sopt.domain.auth.domain.model.AccessTokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * AccessTokenBlacklist 엔티티에 대한 Spring Data JPA 저장소.
 */
public interface AccessTokenBlacklistJpaRepository extends JpaRepository<AccessTokenBlacklist, Long> {

    /**
     * JWT ID가 블랙리스트에 존재하는지 확인한다.
     *
     * @param tokenId JWT ID
     * @return 존재 여부
     */
    boolean existsByTokenId(String tokenId);
}
