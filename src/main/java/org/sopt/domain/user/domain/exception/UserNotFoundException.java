package org.sopt.domain.user.domain.exception;

import org.sopt.global.exception.BaseException;

import java.util.Map;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException(Long userId) {
        super(UserErrorCode.USER_NOT_FOUND, Map.of("userId", userId));
    }
}
