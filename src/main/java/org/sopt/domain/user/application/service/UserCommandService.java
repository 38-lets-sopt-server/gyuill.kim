package org.sopt.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.auth.domain.service.AuthTokenService;
import org.sopt.global.security.authentication.UserRoleCache;
import org.sopt.domain.user.application.dto.CreateUserCommand;
import org.sopt.domain.user.application.dto.UpdateUserCommand;
import org.sopt.domain.user.application.dto.UserResult;
import org.sopt.domain.user.application.mapper.UserResultMapper;
import org.sopt.domain.user.domain.exception.UserErrorCode;
import org.sopt.domain.user.domain.exception.UserNotFoundException;
import org.sopt.domain.user.domain.model.User;
import org.sopt.domain.user.domain.repository.UserRepository;
import org.sopt.global.exception.BaseException;
import org.sopt.global.security.authentication.AuthenticatedUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 생성, 수정, 삭제를 담당하는 command 서비스.
 * 사용자 본인에 대한 수정/삭제는 인증 사용자 ID를 기준으로 처리한다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {

    private final UserRepository userRepository;
    private final AuthTokenService authTokenService;
    private final UserRoleCache userRoleCache;
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자를 생성한다.
     *
     * @param command 생성 입력값
     * @return 생성된 사용자 결과
     */
    public UserResult createUser(CreateUserCommand command) {
        if (userRepository.existsByLoginId(command.loginId())) {
            throw new BaseException(UserErrorCode.USER_LOGIN_ID_DUPLICATED);
        }
        String encodedPassword = passwordEncoder.encode(command.password());
        User user = userRepository.save(new User(command.loginId(), command.nickname(), encodedPassword));
        return UserResultMapper.toResult(user);
    }

    /**
     * 인증된 사용자 본인의 닉네임을 수정한다.
     *
     * @param authenticatedUserId 인증 사용자 ID
     * @param command 수정 입력값
     */
    public void updateUser(Long authenticatedUserId, UpdateUserCommand command) {
        User user = findUserOrThrow(authenticatedUserId);
        user.updateNickname(command.nickname());
    }

    /**
     * 인증된 사용자 본인을 소프트 삭제하고 인증 토큰을 회수한다.
     *
     * @param authenticatedUser 인증 사용자
     */
    public void deleteUser(AuthenticatedUser authenticatedUser) {
        User user = findUserOrThrow(authenticatedUser.userId());
        user.markDeleted();
        authTokenService.revoke(authenticatedUser);
        userRoleCache.evict(authenticatedUser.userId());
    }

    /**
     * 존재하는 사용자를 조회하거나 예외를 던진다.
     *
     * @param id 사용자 ID
     * @return 사용자 엔티티
     */
    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
