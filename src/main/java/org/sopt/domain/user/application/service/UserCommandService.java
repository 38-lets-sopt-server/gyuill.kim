package org.sopt.domain.user.application.service;

import org.sopt.domain.user.application.dto.CreateUserCommand;
import org.sopt.domain.user.application.dto.UpdateUserCommand;
import org.sopt.domain.user.application.dto.UserResult;
import org.sopt.domain.user.application.port.PostPort;
import org.sopt.domain.user.domain.exception.UserHasPostsException;
import org.sopt.domain.user.domain.exception.UserNotFoundException;
import org.sopt.domain.user.domain.model.User;
import org.sopt.domain.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserCommandService {

    private final UserRepository userRepository;
    private final PostPort postPort;

    public UserCommandService(UserRepository userRepository, PostPort postPort) {
        this.userRepository = userRepository;
        this.postPort = postPort;
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
        if (postPort.existsByAuthorUserId(id)) {
            throw new UserHasPostsException();
        }
        userRepository.deleteById(id);
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }
}
