package org.sopt.global.config;

import lombok.RequiredArgsConstructor;
import org.sopt.global.security.authentication.JwtAuthenticationFilter;
import org.sopt.global.security.handler.RestAccessDeniedHandler;
import org.sopt.global.security.handler.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * JWT 기반 stateless 인증 설정.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;

    /**
     * 보안 필터 체인을 구성한다.
     *
     * <p>CSRF 정책: 인증은 access token(Authorization 헤더)을 기반으로 하며,
     * refresh token 쿠키는 Path=/auth + HttpOnly + SameSite로 보호한다.
     * 헤더 인증은 CSRF에 면역이고, 쿠키 운반 경로는 SameSite로 크로스 사이트 자동 전송이 차단되므로
     * Spring Security의 CSRF 필터를 비활성화한다.</p>
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                        .accessDeniedHandler(restAccessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/reissue", "/auth/oauth/google").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/posts", "/posts/search", "/posts/{postId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**", "/actuator/health").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * {@code {bcrypt}} 프리픽스 기반의 DelegatingPasswordEncoder를 제공한다.
     * 새로 저장되는 비밀번호는 {@code {bcrypt}} 프리픽스가 붙어 알고리즘을 식별하며,
     * 향후 Argon2id 등으로 점진적 마이그레이션이 가능하다.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
