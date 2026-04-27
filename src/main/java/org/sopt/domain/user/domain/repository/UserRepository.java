package org.sopt.domain.user.domain.repository;

import org.sopt.domain.user.domain.model.User;

import java.util.List;
import java.util.Optional;

/**
 * User 도메인이 의존하는 저장소 추상화.
 */
public interface UserRepository {

    /**
     * 사용자를 저장한다.
     *
     * @param user 저장할 사용자
     * @return 저장된 사용자
     */
    User save(User user);

    /**
     * 삭제되지 않은 전체 사용자 목록을 조회한다.
     *
     * @return 사용자 목록
     */
    List<User> findAll();

    /**
     * 삭제되지 않은 사용자를 ID로 조회한다.
     *
     * @param id 사용자 ID
     * @return 사용자 조회 결과
     */
    Optional<User> findById(Long id);
}
