package org.sopt.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.global.code.ErrorCode;
import org.sopt.global.code.SuccessCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Schema(description = "공통 API 응답 래퍼")
public record CommonApiResponse<T>(
        @Schema(description = "응답 코드", example = "POST-201")
        String code,
        @Schema(description = "성공 여부", example = "true")
        boolean success,
        @Schema(description = "응답 메시지", example = "게시글 생성 성공")
        String message,
        @Schema(description = "응답 데이터")
        T data,
        @Schema(description = "실패 시 부가 상세 정보", example = "{\"postId\":1}", nullable = true)
        Map<String, Object> details
) {

    public static <T> ResponseEntity<CommonApiResponse<T>> successResponse(SuccessCode successCode, T data) {
        if (successCode.getHttpStatus() == HttpStatus.NO_CONTENT) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity
                .status(successCode.getHttpStatus())
                .body(successBody(successCode, data));
    }

    public static <T> CommonApiResponse<T> successBody(SuccessCode successCode, T data) {
        return new CommonApiResponse<>(successCode.getCode(), true, successCode.getMessage(), data, null);
    }

    public static <T> CommonApiResponse<T> failureBody(ErrorCode errorCode) {
        return new CommonApiResponse<>(errorCode.getCode(), false, errorCode.getMessage(), null, null);
    }

    public static <T> CommonApiResponse<T> failureBody(ErrorCode errorCode, Map<String, Object> details) {
        return new CommonApiResponse<>(errorCode.getCode(), false, errorCode.getMessage(), null, details);
    }
}
