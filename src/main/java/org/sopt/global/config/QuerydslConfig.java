package org.sopt.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Querydsl 쿼리 조립에 사용하는 {@link JPAQueryFactory} 등록 설정.
 */
@Configuration
public class QuerydslConfig {

    /**
     * 애플리케이션 전역에서 재사용할 Querydsl 팩토리를 생성한다.
     *
     * @param entityManager JPA 엔티티 매니저
     * @return Querydsl 팩토리
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
