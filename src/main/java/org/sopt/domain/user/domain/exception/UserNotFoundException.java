package org.sopt.domain.user.domain.exception;

import org.sopt.global.exception.BaseException;

import java.util.Map;

/**
 * 사용자를 찾을 수 없을 때 발생하는 예외.
 */
public class UserNotFoundException extends BaseException {

    /**
     * 상세 정보에 조회 대상 사용자 ID를 담아 예외를 생성한다.
     *
     * @param userId 조회에 실패한 사용자 ID
     */
    public UserNotFoundException(Long userId) {
        super(UserErrorCode.USER_NOT_FOUND, Map.of("userId", userId));
    }
}
