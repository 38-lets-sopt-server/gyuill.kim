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
/**
 * 애플리케이션 전역 예외를 공통 응답 형식으로 변환하는 핸들러.
 * DB 제약 위반과 JPA 시스템 오류를 같은 400으로 묶지 않고 성격에 따라 분리하였습니다.
 */
public class GlobalExceptionHandler {

    // TODO: 롬복이 있었다면 그냥 slf4j를 썻지만 그러지 못하지 직접 추가합니다. 롬복 풀리면 변경 예정!
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 도메인 비즈니스 예외를 각 에러 코드에 맞는 응답으로 변환한다.
     *
     * @param e 비즈니스 예외
     * @return 공통 실패 응답
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<CommonApiResponse<Void>> handleBaseException(BaseException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("Business exception: {}", errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(CommonApiResponse.failureBody(errorCode, e.getDetails()));
    }

    /**
     * JSON 파싱 실패를 잘못된 요청으로 처리한다.
     *
     * @param e 요청 본문 파싱 예외
     * @return 공통 실패 응답
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonApiResponse<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("Request body is not readable: {}", e.getMessage());
        return CommonApiResponse.failureBody(GlobalErrorCode.INVALID_REQUEST);
    }

    /**
     * 단순 입력값 예외를 잘못된 요청으로 처리한다.
     *
     * @param e 입력값 예외
     * @return 공통 실패 응답
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonApiResponse<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Validation failed: {}", e.getMessage());
        return CommonApiResponse.failureBody(GlobalErrorCode.INVALID_REQUEST);
    }

    /**
     * DB 제약 위반을 사용자 입력 오류에 가까운 400 응답으로 처리한다.
     * 현재로써는 발생할 확률이 극히 낮은데요. dto단에서 먼저 검증을 하고 있고 다른 객체 접근 경로가 없으니까요.
     * 세미나에서 소연님이 말했듯이 개발자의 실수 리스크나 확장을 고려해서 추가하는게 맞다고 생각했습니다.
     *
     * @param e 데이터 무결성 예외
     * @return 공통 실패 응답
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonApiResponse<Void> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.warn("Database constraint violation: {}", e.getMessage());
        return CommonApiResponse.failureBody(GlobalErrorCode.INVALID_REQUEST);
    }

    /**
     * JPA/트랜잭션 시스템 오류를 서버 내부 문제로 처리한다.
     *
     * @param e 영속성 계층 시스템 예외
     * @return 공통 실패 응답
     */
    @ExceptionHandler({
            JpaSystemException.class,
            TransactionSystemException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonApiResponse<Void> handlePersistenceException(Exception e) {
        log.error("Persistence system error occurred", e);
        return CommonApiResponse.failureBody(GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * 등록되지 않은 경로 요청을 404 응답으로 처리한다.
     * 앱잼을 할 때 서비스를 배포해놨는데 자꾸 인터넷을 돌아다니는 봇들이 없는 경로에 요청을 하면서 500을 발생시키더라고요.
     * 그래서 그 이후부터 추가하게 된 핸들러입니다.
     * 또한 잘못된 Http Method로 요청했을 때 올바른 경로를 안내하는게 HTTP 규격이기도 해서 추가해야합니다.
     * (근데 이건 구현 안돼있어요, 필터단에서 구현하는게 나은데 아직 인증/인가까지 진도를 안나갔으니까요.)
     *
     * @param e 리소스 미존재 예외
     * @return 공통 실패 응답
     */
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonApiResponse<Void> handleNoResourceFoundException(NoResourceFoundException e) {
        log.debug("Resource not found: {}", e.getResourcePath());
        return CommonApiResponse.failureBody(GlobalErrorCode.RESOURCE_NOT_FOUND);
    }

    /**
     * 예상하지 못한 나머지 예외를 500 응답으로 처리한다.
     *
     * @param e 예기치 못한 예외
     * @return 공통 실패 응답
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonApiResponse<Void> handleException(Exception e) {
        log.error("Unexpected error occurred", e);
        return CommonApiResponse.failureBody(GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }
}
