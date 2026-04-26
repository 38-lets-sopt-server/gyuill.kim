package org.sopt.domain.user.domain.exception;

import org.sopt.global.exception.BaseException;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException() {
        super(UserErrorCode.USER_NOT_FOUND);
    }
}
