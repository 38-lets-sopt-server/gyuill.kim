package org.sopt.domain.user.application.service;

import org.sopt.domain.user.application.dto.UserResult;
import org.sopt.domain.user.domain.exception.UserNotFoundException;
import org.sopt.domain.user.domain.model.User;
import org.sopt.domain.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 사용자 조회를 담당하는 query 서비스.
 */
@Service
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;

    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 삭제되지 않은 전체 사용자 목록을 조회한다.
     *
     * @return 사용자 결과 목록
     */
    public List<UserResult> getUsers() {
        return userRepository.findAll().stream()
                .map(this::toUserResult)
                .toList();
    }

    /**
     * 사용자 단건을 조회한다.
     *
     * @param id 사용자 ID
     * @return 사용자 결과
     */
    public UserResult getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return toUserResult(user);
    }

    /**
     * 사용자 엔티티를 조회 응답용 결과 모델로 변환한다.
     *
     * @param user 사용자 엔티티
     * @return 사용자 결과
     */
    private UserResult toUserResult(User user) {
        return new UserResult(
                user.getId(),
                user.getNickname(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
