package org.sopt.global.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopt.global.code.ErrorCode;
import org.sopt.global.code.GlobalErrorCode;
import org.sopt.global.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 커스텀 예외 처리
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("Business exception: {}", errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new ApiResponse<>(false, errorCode.getMessage(), null));
    }

    // JSON 파싱 실패 처리
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("Request body is not readable: {}", e.getMessage());
        return new ApiResponse<>(false, GlobalErrorCode.INVALID_REQUEST.getMessage(), null);
    }

    // 존재하지 않는 리소스 요청 처리
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleNoResourceFound(NoResourceFoundException e) {
        log.debug("Resource not found: {}", e.getResourcePath());
        return new ApiResponse<>(false, GlobalErrorCode.RESOURCE_NOT_FOUND.getMessage(), null);
    }

    // 그 외 모든 예외 처리
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleException(Exception e) {
        log.error("Unexpected error occurred", e);
        return new ApiResponse<>(false, GlobalErrorCode.INTERNAL_SERVER_ERROR.getMessage(), null);
    }
}
