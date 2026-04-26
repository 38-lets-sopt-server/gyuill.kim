package org.sopt.global.code;

import org.springframework.http.HttpStatus;

public interface ApiCode {

    String getCode();

    HttpStatus getHttpStatus();

    String getMessage();
}
