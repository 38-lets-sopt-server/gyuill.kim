package org.sopt.domain.user.infrastructure;

import org.sopt.domain.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    List<User> findAllByDeletedAtIsNull();

    Optional<User> findByIdAndDeletedAtIsNull(Long id);
}
