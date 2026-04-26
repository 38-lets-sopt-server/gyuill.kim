package org.sopt.domain.post.application.port;

import org.sopt.domain.user.domain.model.User;

public interface UserPort {

    User getUser(Long userId);
}
