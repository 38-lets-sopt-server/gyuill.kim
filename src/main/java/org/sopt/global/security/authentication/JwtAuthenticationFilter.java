package org.sopt.global.security.authentication;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sopt.domain.auth.domain.repository.AccessTokenBlacklistRepository;
import org.sopt.domain.user.domain.model.User;
import org.sopt.domain.user.domain.model.UserRole;
import org.sopt.domain.user.domain.repository.UserRepository;
import org.sopt.global.security.exception.JwtAuthenticationException;
import org.sopt.global.security.jwt.JwtTokenPayload;
import org.sopt.global.security.jwt.JwtTokenProvider;
import org.sopt.global.security.jwt.JwtTokenType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

/**
 * Authorization Bearer 토큰을 읽어 SecurityContext에 인증 사용자를 저장한다.
 *
 * <p>사용자 역할 조회 결과를 Caffeine 캐시(5분 TTL)에 보관하여
 * 매 요청마다 DB를 조회하는 비용을 절감한다.</p>
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final BearerTokenResolver bearerTokenResolver;
    private final JwtTokenProvider jwtTokenProvider;
    private final AccessTokenBlacklistRepository accessTokenBlacklistRepository;
    private final UserRepository userRepository;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final Cache<Long, UserRole> userRoleCache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(Duration.ofMinutes(5))
            .build();

    public JwtAuthenticationFilter(
            BearerTokenResolver bearerTokenResolver,
            JwtTokenProvider jwtTokenProvider,
            AccessTokenBlacklistRepository accessTokenBlacklistRepository,
            UserRepository userRepository,
            AuthenticationEntryPoint authenticationEntryPoint
    ) {
        this.bearerTokenResolver = bearerTokenResolver;
        this.jwtTokenProvider = jwtTokenProvider;
        this.accessTokenBlacklistRepository = accessTokenBlacklistRepository;
        this.userRepository = userRepository;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = bearerTokenResolver.resolve(request);
        try {
            if (token != null) {
                JwtTokenPayload payload = jwtTokenProvider.getPayload(token, JwtTokenType.ACCESS);
                if (accessTokenBlacklistRepository.exists(payload.tokenId())) {
                    throw new JwtAuthenticationException("Access token is blacklisted.");
                }
                UserRole role = userRoleCache.get(payload.userId(), this::loadUserRole);
                if (role == null) {
                    throw new JwtAuthenticationException("User is inactive.");
                }
                UsernamePasswordAuthenticationToken authentication = createAuthentication(payload, role);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtAuthenticationException e) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, e);
            return;
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 사용자 역할 캐시에서 해당 사용자를 제거한다.
     * 역할 변경이나 사용자 삭제 시 호출하여 캐시 정합성을 유지한다.
     *
     * @param userId 캐시에서 제거할 사용자 ID
     */
    public void evictUserRole(Long userId) {
        userRoleCache.invalidate(userId);
    }

    private UserRole loadUserRole(Long userId) {
        return userRepository.findById(userId)
                .map(User::getRole)
                .orElse(null);
    }

    private static UsernamePasswordAuthenticationToken createAuthentication(JwtTokenPayload payload, UserRole role) {
        AuthenticatedUser principal = new AuthenticatedUser(
                payload.userId(),
                role,
                payload.tokenId(),
                payload.expiresAt()
        );

        return new UsernamePasswordAuthenticationToken(
                principal,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + role.name()))
        );
    }
}
