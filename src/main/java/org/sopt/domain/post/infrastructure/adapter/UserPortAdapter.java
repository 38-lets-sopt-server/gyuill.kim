package org.sopt.domain.post.infrastructure.adapter;

import org.sopt.domain.post.application.port.UserPort;
import org.sopt.domain.user.domain.exception.UserNotFoundException;
import org.sopt.domain.user.domain.model.User;
import org.sopt.domain.user.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

/**
 * Post 도메인용 {@link UserPort} 구현체.
 * 추가적인 설명은 UserPort.java에 적어놨습니다.
 * User 저장소를 직접 노출하지 않고 필요한 조회 기능만 어댑터를 통해 제공한다.
 */
@Component
public class UserPortAdapter implements UserPort {

    private final UserRepository userRepository;

    public UserPortAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 사용자를 조회하거나 예외를 던진다.
     *
     * @param userId 사용자 ID
     * @return 사용자 엔티티
     */
    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
