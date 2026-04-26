package org.sopt.domain.user.domain.exception;

import org.sopt.global.exception.BaseException;

public class UserHasPostsException extends BaseException {

    public UserHasPostsException() {
        super(UserErrorCode.USER_HAS_POSTS);
    }
}
