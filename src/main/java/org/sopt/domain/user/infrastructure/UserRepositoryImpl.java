package org.sopt.domain.user.infrastructure;

import org.sopt.domain.user.domain.model.User;
import org.sopt.domain.user.domain.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User 도메인 저장소를 JPA 구현체에 연결하는 어댑터.
 * 여기도 현재 역할이 구현체보다는 어댑터에 가깝지만 확장성과 일관성을 고려해 impl이라 지었습니다.
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    /**
     * 사용자를 저장한다.
     *
     * @param user 저장할 사용자
     * @return 저장된 사용자
     */
    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    /**
     * 삭제되지 않은 사용자만 조회한다.
     *
     * @return 사용자 목록
     */
    @Override
    public List<User> findAll() {
        return userJpaRepository.findAllByDeletedAtIsNull();
    }

    /**
     * 삭제되지 않은 사용자를 단건 조회한다.
     *
     * @param id 사용자 ID
     * @return 사용자 조회 결과
     */
    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findByIdAndDeletedAtIsNull(id);
    }
}
