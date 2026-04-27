package org.sopt.global.swagger;

import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.web.method.HandlerMethod;
import org.sopt.global.annotation.ApiExceptions;

public class SwaggerOperationCustomizer implements OperationCustomizer {

    private final SwaggerErrorExampleGenerator swaggerErrorExampleGenerator;

    public SwaggerOperationCustomizer(SwaggerErrorExampleGenerator swaggerErrorExampleGenerator) {
        this.swaggerErrorExampleGenerator = swaggerErrorExampleGenerator;
    }

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        ApiExceptions apiExceptions = handlerMethod.getMethodAnnotation(ApiExceptions.class);
        if (apiExceptions != null) {
            swaggerErrorExampleGenerator.addErrorResponses(operation, apiExceptions.value());
        }
        return operation;
    }
}
