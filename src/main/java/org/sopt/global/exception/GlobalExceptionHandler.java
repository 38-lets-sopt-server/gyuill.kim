package org.sopt.global.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopt.global.code.ErrorCode;
import org.sopt.global.code.GlobalErrorCode;
import org.sopt.global.response.CommonApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 커스텀 예외 처리
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<CommonApiResponse<Void>> handleBaseException(BaseException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("Business exception: {}", errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(CommonApiResponse.failure(errorCode, e.getDetails()));
    }

    // JSON 파싱 실패 처리
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonApiResponse<Void> handleMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("Request body is not readable: {}", e.getMessage());
        return CommonApiResponse.failure(GlobalErrorCode.INVALID_REQUEST);
    }

    // 입력값 검증 실패 처리
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonApiResponse<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Validation failed: {}", e.getMessage());
        return CommonApiResponse.failure(GlobalErrorCode.INVALID_REQUEST);
    }

    // DB 제약조건 위반 처리
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonApiResponse<Void> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.warn("Database constraint violation: {}", e.getMessage());
        return CommonApiResponse.failure(GlobalErrorCode.INVALID_REQUEST);
    }

    // JPA/트랜잭션 시스템 오류 처리
    @ExceptionHandler({
            JpaSystemException.class,
            TransactionSystemException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonApiResponse<Void> handlePersistenceSystemException(Exception e) {
        log.error("Persistence system error occurred", e);
        return CommonApiResponse.failure(GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }

    // 존재하지 않는 리소스 요청 처리
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonApiResponse<Void> handleNoResourceFound(NoResourceFoundException e) {
        log.debug("Resource not found: {}", e.getResourcePath());
        return CommonApiResponse.failure(GlobalErrorCode.RESOURCE_NOT_FOUND);
    }

    // 그 외 모든 예외 처리
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonApiResponse<Void> handleException(Exception e) {
        log.error("Unexpected error occurred", e);
        return CommonApiResponse.failure(GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }
}
