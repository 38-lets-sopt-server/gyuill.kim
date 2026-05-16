package org.sopt.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Spring Retry 활성화 설정.
 * 낙관적 락 충돌 같은 일시적 실패를 어노테이션 기반으로 재시도하기 위해 사용한다.
 */
@Configuration
@EnableRetry
public class RetryConfig {
}
