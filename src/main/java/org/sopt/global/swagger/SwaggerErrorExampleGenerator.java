package org.sopt.global.swagger;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.sopt.global.code.ErrorCode;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SwaggerErrorExampleGenerator {

    private static final String APPLICATION_JSON = "application/json";

    public void addErrorResponses(Operation operation, Class<? extends Enum<?>>[] errorEnums) {
        ApiResponses responses = operation.getResponses();
        List<ErrorCode> errorCodes = extractErrorCodes(errorEnums);
        Map<Integer, List<ErrorCode>> errorCodesByStatus = errorCodes.stream()
                .collect(Collectors.groupingBy(
                        errorCode -> errorCode.getHttpStatus().value(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        errorCodesByStatus.forEach((status, codes) ->
                responses.addApiResponse(String.valueOf(status), createApiResponse(codes))
        );
    }

    private List<ErrorCode> extractErrorCodes(Class<? extends Enum<?>>[] errorEnums) {
        return Arrays.stream(errorEnums)
                .filter(ErrorCode.class::isAssignableFrom)
                .flatMap(errorEnum -> Arrays.stream(errorEnum.getEnumConstants()))
                .map(constant -> (ErrorCode) constant)
                .toList();
    }

    private ApiResponse createApiResponse(List<ErrorCode> errorCodes) {
        MediaType mediaType = new MediaType();
        errorCodes.forEach(errorCode -> mediaType.addExamples(
                ((Enum<?>) errorCode).name(),
                createExample(errorCode)
        ));

        Content content = new Content();
        content.addMediaType(APPLICATION_JSON, mediaType);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setDescription(errorCodes.get(0).getHttpStatus().getReasonPhrase());
        apiResponse.setContent(content);
        return apiResponse;
    }

    private Example createExample(ErrorCode errorCode) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", errorCode.getCode());
        body.put("success", false);
        body.put("message", errorCode.getMessage());
        body.put("data", null);
        body.put("details", null);

        Example example = new Example();
        example.setSummary(errorCode.getMessage());
        example.setValue(body);
        return example;
    }
}
