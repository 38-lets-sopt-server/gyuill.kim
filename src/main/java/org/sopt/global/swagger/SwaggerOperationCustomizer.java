package org.sopt.global.swagger;

import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.web.method.HandlerMethod;
import org.sopt.global.annotation.ApiExceptions;

/**
 * 컨트롤러 메서드의 {@link ApiExceptions} 선언을 읽어 Swagger 실패 응답을 보강하는 커스터마이저.
 * 여기도 제가 사용하는 swagger 템플릿!
 */
public class SwaggerOperationCustomizer implements OperationCustomizer {

    private final SwaggerErrorExampleGenerator swaggerErrorExampleGenerator;

    public SwaggerOperationCustomizer(SwaggerErrorExampleGenerator swaggerErrorExampleGenerator) {
        this.swaggerErrorExampleGenerator = swaggerErrorExampleGenerator;
    }

    /**
     * 메서드에 선언된 에러 코드 enum을 Swagger 응답 예시에 반영한다.
     *
     * @param operation Swagger operation
     * @param handlerMethod 스프링 핸들러 메서드
     * @return 수정된 operation
     */
    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        ApiExceptions apiExceptions = handlerMethod.getMethodAnnotation(ApiExceptions.class);
        if (apiExceptions != null) {
            swaggerErrorExampleGenerator.addErrorResponses(operation, apiExceptions.value());
        }
        return operation;
    }
}
