package org.sopt.domain.post.infrastructure.adapter;

import org.sopt.domain.post.application.port.UserPort;
import org.sopt.domain.user.domain.exception.UserNotFoundException;
import org.sopt.domain.user.domain.model.User;
import org.sopt.domain.user.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserPortAdapter implements UserPort {

    private final UserRepository userRepository;

    public UserPortAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }
}
