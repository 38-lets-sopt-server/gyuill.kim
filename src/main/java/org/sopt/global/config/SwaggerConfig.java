package org.sopt.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sopt.global.swagger.SwaggerErrorExampleGenerator;
import org.sopt.global.swagger.SwaggerOperationCustomizer;

import java.util.List;

@Configuration
/**
 * OpenAPI 메타데이터와 예외 응답 커스터마이저를 등록하는 Swagger 설정.
 */
public class SwaggerConfig {

    /**
     * 과제 제출용 OpenAPI 문서 기본 정보를 정의한다.
     *
     * @return OpenAPI 설정
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gyuill Kim Assignment API")
                        .version("v1")
                        .description("자유게시판 및 사용자 관리 과제 API 문서"))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Server")
                ));
    }

    /**
     * {@link ApiExceptions} 선언을 읽어 상태 코드별 실패 예시를 붙이는 커스터마이저를 등록한다.
     *
     * @return Swagger operation customizer
     */
    @Bean
    public OperationCustomizer swaggerOperationCustomizer() {
        return new SwaggerOperationCustomizer(new SwaggerErrorExampleGenerator());
    }
}
