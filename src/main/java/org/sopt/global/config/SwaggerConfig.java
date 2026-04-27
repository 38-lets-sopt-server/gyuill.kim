package org.sopt.global.config;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sopt.global.swagger.SwaggerErrorExampleGenerator;
import org.sopt.global.swagger.SwaggerOperationCustomizer;

@Configuration
public class SwaggerConfig {

    @Bean
    public OperationCustomizer swaggerOperationCustomizer() {
        return new SwaggerOperationCustomizer(new SwaggerErrorExampleGenerator());
    }
}
