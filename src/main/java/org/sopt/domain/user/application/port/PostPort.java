package org.sopt.domain.user.application.port;

public interface PostPort {

    boolean existsByAuthorUserId(Long authorUserId);
}
