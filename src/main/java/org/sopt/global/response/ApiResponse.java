package org.sopt.global.response;

import org.sopt.global.code.ErrorCode;
import org.sopt.global.code.SuccessCode;

public record ApiResponse<T>(String code, boolean success, String message, T data) {

    public static <T> ApiResponse<T> success(SuccessCode successCode, T data) {
        return new ApiResponse<>(successCode.getCode(), true, successCode.getMessage(), data);
    }

    public static <T> ApiResponse<T> failure(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getCode(), false, errorCode.getMessage(), null);
    }
}
