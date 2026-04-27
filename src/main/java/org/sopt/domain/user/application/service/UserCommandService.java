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
        return toUserResult(user);
    }

    private UserResult toUserResult(User user) {
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
        User user = findUserOrThrow(id);
        user.markDeleted();
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
