package org.sopt.domain.auth.infrastructure;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.auth.domain.model.AccessTokenBlacklist;
import org.sopt.domain.auth.domain.repository.AccessTokenBlacklistRepository;
import org.springframework.stereotype.Repository;

/**
 * access token 블랙리스트 저장소를 JPA 구현체에 연결하는 어댑터.
 */
@Repository
@RequiredArgsConstructor
public class AccessTokenBlacklistRepositoryImpl implements AccessTokenBlacklistRepository {

    private final AccessTokenBlacklistJpaRepository accessTokenBlacklistJpaRepository;

    /**
     * 블랙리스트를 저장한다.
     *
     * @param accessTokenBlacklist 저장할 블랙리스트
     * @return 저장된 블랙리스트
     */
    @Override
    public AccessTokenBlacklist save(AccessTokenBlacklist accessTokenBlacklist) {
        return accessTokenBlacklistJpaRepository.save(accessTokenBlacklist);
    }

    /**
     * JWT ID가 블랙리스트에 존재하는지 확인한다.
     *
     * @param tokenId JWT ID
     * @return 존재 여부
     */
    @Override
    public boolean existsByTokenId(String tokenId) {
        return accessTokenBlacklistJpaRepository.existsByTokenId(tokenId);
    }
}
