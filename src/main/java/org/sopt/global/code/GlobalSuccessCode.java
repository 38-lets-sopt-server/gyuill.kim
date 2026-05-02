package org.sopt.global.code;

import org.springframework.http.HttpStatus;

/**
 * 전역 공통 성공 코드 모음.
 * 요즘엔 이거에 대한 고민을 하고 있어요. 결국 코드를 발전키시다보면 각 엔드포인트 별로 성공 응답을 만들게 되고
 * 그러면 global success code를 안쓰게 됩니다.
 * 그래도 아직은 하위 호환성이나 개발 편의를 위해너 놔두는게 맞지 않을까 하는 생각으로 만들어두고 있습니다.
 */
public enum GlobalSuccessCode implements SuccessCode {

    OK("GLB-S001", HttpStatus.OK, "요청이 성공했습니다."),
    CREATED("GLB-S002", HttpStatus.CREATED, "리소스가 생성되었습니다."),
    NO_CONTENT("GLB-S003", HttpStatus.NO_CONTENT, "요청이 성공적으로 처리되었습니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    GlobalSuccessCode(String code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
