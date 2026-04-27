package org.sopt.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
/**
 * 세미나에서도 봤던 BaseEntity를 위한 config
 * 생성/수정 시각 자동 기록을 활성화하는 JPA Auditing 설정.
 */
public class JpaAuditConfig {
}
