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
public class SwaggerConfig {

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

    @Bean
    public OperationCustomizer swaggerOperationCustomizer() {
        return new SwaggerOperationCustomizer(new SwaggerErrorExampleGenerator());
    }
}
