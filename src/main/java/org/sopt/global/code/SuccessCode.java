package org.sopt.global.code;

import org.springframework.http.HttpStatus;

public interface SuccessCode {

    String getCode();

    HttpStatus getHttpStatus();

    String getMessage();
}
