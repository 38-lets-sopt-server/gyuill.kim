package org.sopt.domain.user.application.mapper;

import org.sopt.domain.user.application.dto.UserResult;
import org.sopt.domain.user.domain.model.User;

/**
 * 사용자 엔티티를 애플리케이션 결과 모델로 변환하는 유틸 매퍼.
 */
public final class UserResultMapper {

    private UserResultMapper() {
    }

    /**
     * 사용자 엔티티를 결과 모델로 변환한다.
     *
     * @param user 사용자 엔티티
     * @return 사용자 결과
     */
    public static UserResult toResult(User user) {
        return new UserResult(
                user.getId(),
                user.getNickname(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
