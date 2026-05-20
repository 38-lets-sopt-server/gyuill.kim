package org.sopt.global.security.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.auth.domain.repository.AccessTokenBlacklistRepository;
import org.sopt.domain.user.domain.model.User;
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
import java.util.List;

/**
 * Authorization Bearer 토큰을 읽어 SecurityContext에 인증 사용자를 저장한다.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final BearerTokenResolver bearerTokenResolver;
    private final JwtTokenProvider jwtTokenProvider;
    private final AccessTokenBlacklistRepository accessTokenBlacklistRepository;
    private final UserRepository userRepository;
    private final AuthenticationEntryPoint authenticationEntryPoint;

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
                if (accessTokenBlacklistRepository.existsByTokenId(payload.tokenId())) {
                    throw new JwtAuthenticationException("Access token is blacklisted.");
                }
                User user = userRepository.findById(payload.userId())
                        .orElseThrow(() -> new JwtAuthenticationException("User is inactive."));
                UsernamePasswordAuthenticationToken authentication = createAuthentication(payload, user);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtAuthenticationException e) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, e);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private static UsernamePasswordAuthenticationToken createAuthentication(JwtTokenPayload payload, User user) {
        AuthenticatedUser principal = new AuthenticatedUser(
                payload.userId(),
                user.getRole(),
                payload.tokenId(),
                payload.expiresAt()
        );

        return new UsernamePasswordAuthenticationToken(
                principal,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
