package com.ninghua.common.core.exception;

/**
 * @Author Derek.Fung
 * @Date 2025/5/7 11:14
 **/
public class ValidateCodeException extends RuntimeException {

    public ValidateCodeException() {
    }

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
