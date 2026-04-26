package org.sopt.global.response;

import org.sopt.global.code.ErrorCode;
import org.sopt.global.code.SuccessCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public record CommonApiResponse<T>(String code, boolean success, String message, T data, Map<String, Object> details) {

    public static <T> ResponseEntity<CommonApiResponse<T>> success(SuccessCode successCode, T data) {
        if (successCode.getHttpStatus() == HttpStatus.NO_CONTENT) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity
                .status(successCode.getHttpStatus())
                .body(new CommonApiResponse<>(successCode.getCode(), true, successCode.getMessage(), data, null));
    }

    public static <T> CommonApiResponse<T> failure(ErrorCode errorCode) {
        return new CommonApiResponse<>(errorCode.getCode(), false, errorCode.getMessage(), null, null);
    }

    public static <T> CommonApiResponse<T> failure(ErrorCode errorCode, Map<String, Object> details) {
        return new CommonApiResponse<>(errorCode.getCode(), false, errorCode.getMessage(), null, details);
    }
}
