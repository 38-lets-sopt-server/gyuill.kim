package org.sopt.global.exception;

import org.sopt.global.code.ErrorCode;

import java.util.Map;

/**
 * 비즈니스 오류 코드와 구조화된 상세 정보를 함께 전달하는 기본 예외입니다.
 * 클라이언트에게 보다 구체적인 예회의 정보를 보내주기 위해 details 필드를 추가했습니다.
 */
public class BaseException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    /**
     * 상세 정보 없이 에러 코드만으로 예외를 생성한다.
     *
     * @param errorCode 비즈니스 에러 코드
     */
    public BaseException(ErrorCode errorCode) {
        this(errorCode, Map.of());
    }

    /**
     * 에러 코드와 상세 정보를 함께 담아 예외를 생성한다.
     *
     * @param errorCode 비즈니스 에러 코드
     * @param details 디버깅 및 클라이언트 보조 처리를 위한 구조화 상세 정보
     */
    public BaseException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = details;
    }

    /**
     * 응답 변환에 사용할 에러 코드를 반환한다.
     *
     * @return 에러 코드
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * 응답의 {@code details} 필드로 내려갈 구조화 상세 정보를 반환한다.
     *
     * @return 상세 정보 맵
     */
    public Map<String, Object> getDetails() {
        return details;
    }
}
