package org.sopt.global.code;

import org.springframework.http.HttpStatus;

/**
 * 성공/실패 응답 코드가 공통으로 따라야 하는 규약입니다.
 */
public interface ApiCode {

    /**
     * 클라이언트에 노출할 비즈니스 코드 값을 반환한다.
     *
     * @return 응답 코드
     */
    String getCode();

    /**
     * 코드에 대응하는 HTTP 상태를 반환한다.
     *
     * @return HTTP 상태
     */
    HttpStatus getHttpStatus();

    /**
     * 사용자에게 보여줄 기본 메시지를 반환한다.
     *
     * @return 응답 메시지
     */
    String getMessage();
}
