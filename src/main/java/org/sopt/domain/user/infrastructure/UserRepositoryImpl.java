package org.sopt.domain.user.infrastructure;

import org.sopt.domain.user.domain.model.User;
import org.sopt.domain.user.domain.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAllByDeletedAtIsNull();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findByIdAndDeletedAtIsNull(id);
    }
}
