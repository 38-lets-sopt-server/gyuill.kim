package org.sopt.domain.user.domain.repository;

import org.sopt.domain.user.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    List<User> findAll();

    Optional<User> findById(Long id);
}
