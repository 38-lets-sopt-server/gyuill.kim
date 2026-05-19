package org.sopt.global.swagger;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.sopt.global.code.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 에러 코드 enum 정의를 Swagger 실패 응답 예시로 변환하는 클래스.
 * 과제 리뷰 시 예외 응답 구조를 문서에서 바로 확인할 수 있도록 상태 코드별 예시를 자동 생성한다.
 * 이것두 제가 개발 할 때마다 가져다 쓰는 템플릿입니다.
 */
public class SwaggerErrorExampleGenerator {

    private static final String APPLICATION_JSON = "application/json";

    /**
     * 컨트롤러 메서드에 선언된 에러 enum 목록을 읽어 OpenAPI 응답 예시를 추가한다.
     *
     * @param operation 대상 Swagger operation
     * @param errorEnums 에러 코드 enum 목록
     */
    public void addErrorResponses(Operation operation, Class<? extends Enum<?>>[] errorEnums) {
        ApiResponses responses = operation.getResponses();
        List<ErrorCode> errorCodes = extractErrorCodes(errorEnums);
        Map<HttpStatus, List<ErrorCode>> errorCodesByStatus = errorCodes.stream()
                .collect(Collectors.groupingBy(
                        ErrorCode::getHttpStatus,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        errorCodesByStatus.forEach((status, codes) ->
                mergeIntoResponses(responses, status, codes)
        );
    }

    /**
     * enum 상수를 {@link ErrorCode} 목록으로 변환한다.
     *
     * @param errorEnums 에러 코드 enum 목록
     * @return 에러 코드 목록
     */
    private List<ErrorCode> extractErrorCodes(Class<? extends Enum<?>>[] errorEnums) {
        return Arrays.stream(errorEnums)
                .filter(ErrorCode.class::isAssignableFrom)
                .flatMap(errorEnum -> Arrays.stream(errorEnum.getEnumConstants()))
                .map(constant -> (ErrorCode) constant)
                .toList();
    }

    /**
     * 같은 HTTP 상태를 공유하는 에러 코드 목록으로 Swagger 응답 하나를 만든다.
     *
     * @param errorCodes 상태 코드가 같은 에러 코드 목록
     * @return Swagger 응답 객체
     */
    private void mergeIntoResponses(ApiResponses responses, HttpStatus status, List<ErrorCode> errorCodes) {
        String statusKey = String.valueOf(status.value());

        ApiResponse apiResponse = responses.get(statusKey);
        if (apiResponse == null) {
            apiResponse = new ApiResponse().description(status.getReasonPhrase());
            responses.addApiResponse(statusKey, apiResponse);
        }

        Content content = apiResponse.getContent();
        if (content == null) {
            content = new Content();
            apiResponse.setContent(content);
        }

        MediaType mediaType = content.get(APPLICATION_JSON);
        if (mediaType == null) {
            mediaType = new MediaType();
            content.addMediaType(APPLICATION_JSON, mediaType);
        }

        for (ErrorCode errorCode : errorCodes) {
            mediaType.addExamples(errorCode.getCode(), createExample(errorCode));
        }
    }

    /**
     * 개별 에러 코드에 대한 예시 본문을 만든다.
     *
     * @param errorCode 에러 코드
     * @return Swagger example
     */
    private Example createExample(ErrorCode errorCode) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", errorCode.getCode());
        body.put("success", false);
        body.put("message", errorCode.getMessage());
        body.put("data", null);
        body.put("details", null);

        Example example = new Example();
        example.setSummary(errorCode.getCode());
        example.setDescription(errorCode.getMessage());
        example.setValue(body);
        return example;
    }
}
