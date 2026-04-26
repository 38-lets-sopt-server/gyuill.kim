package org.sopt.domain.user.infrastructure;

import org.sopt.domain.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}
