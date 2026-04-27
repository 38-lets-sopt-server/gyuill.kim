package org.sopt.domain.user.application.service;

import org.sopt.domain.user.application.dto.CreateUserCommand;
import org.sopt.domain.user.application.dto.UpdateUserCommand;
import org.sopt.domain.user.application.dto.UserResult;
import org.sopt.domain.user.domain.exception.UserNotFoundException;
import org.sopt.domain.user.domain.model.User;
import org.sopt.domain.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
/**
 * 사용자 생성, 수정, 삭제를 담당하는 command 서비스.
 * 과제 범위에서는 인증/인가 없이 사용자 자체 생명주기만 다룬다.
 */
public class UserCommandService {

    private final UserRepository userRepository;

    public UserCommandService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 사용자를 생성한다.
     *
     * @param command 생성 입력값
     * @return 생성된 사용자 결과
     */
    public UserResult createUser(CreateUserCommand command) {
        User user = userRepository.save(new User(command.nickname()));
        return toUserResult(user);
    }

    /**
     * 사용자 응답용 결과 모델로 변환한다.
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

    /**
     * 사용자 닉네임을 수정한다.
     *
     * @param id 사용자 ID
     * @param command 수정 입력값
     */
    public void updateUser(Long id, UpdateUserCommand command) {
        User user = findUserOrThrow(id);
        user.updateNickname(command.nickname());
    }

    /**
     * 사용자를 소프트 삭제한다.
     *
     * @param id 사용자 ID
     */
    public void deleteUser(Long id) {
        User user = findUserOrThrow(id);
        user.markDeleted();
    }

    /**
     * 존재하는 사용자를 조회하거나 예외를 던진다.
     *
     * @param id 사용자 ID
     * @return 사용자 엔티티
     */
    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
