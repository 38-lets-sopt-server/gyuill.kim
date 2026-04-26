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
public class UserCommandService {

    private final UserRepository userRepository;

    public UserCommandService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResult createUser(CreateUserCommand command) {
        User user = userRepository.save(new User(command.nickname()));
        return new UserResult(
                user.getId(),
                user.getNickname(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public void updateUser(Long id, UpdateUserCommand command) {
        User user = findUserOrThrow(id);
        user.updateNickname(command.nickname());
    }

    public void deleteUser(Long id) {
        findUserOrThrow(id);
        // TODO: 추후 User soft delete 도입 시 게시글 작성 이력 보존 정책과 함께 삭제 정책을 재설계할 예정입니다. 지금 하기에는 너무 복잡해져서 일단 넘길게요. 과제 범위를 괜히 빡세게 잡았다가 너무 힘드네요 휴
        userRepository.deleteById(id);
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
