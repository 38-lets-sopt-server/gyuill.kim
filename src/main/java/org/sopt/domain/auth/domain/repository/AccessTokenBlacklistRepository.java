package org.sopt.domain.auth.domain.repository;

import org.sopt.domain.auth.domain.model.AccessTokenBlacklist;

/**
 * access token 블랙리스트 저장소 추상화.
 */
public interface AccessTokenBlacklistRepository {

    /**
     * 블랙리스트를 저장한다.
     *
     * @param accessTokenBlacklist 저장할 블랙리스트
     * @return 저장된 블랙리스트
     */
    AccessTokenBlacklist save(AccessTokenBlacklist accessTokenBlacklist);

    /**
     * JWT ID가 블랙리스트에 존재하는지 확인한다.
     *
     * @param tokenId JWT ID
     * @return 존재 여부
     */
    boolean existsByTokenId(String tokenId);
}
