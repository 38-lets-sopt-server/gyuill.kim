package org.sopt.domain.user.application.service;

import org.sopt.domain.user.application.dto.UserResult;
import org.sopt.domain.user.domain.exception.UserNotFoundException;
import org.sopt.domain.user.domain.model.User;
import org.sopt.domain.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;

    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResult> getUsers() {
        return userRepository.findAll().stream()
                .map(this::toUserResult)
                .toList();
    }

    public UserResult getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
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
}
