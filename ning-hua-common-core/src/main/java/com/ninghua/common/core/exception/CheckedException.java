package com.ninghua.common.core.exception;

import lombok.NoArgsConstructor;

/**
 * @Author Derek.Fung
 * @Date 2025/5/7 11:23
 **/
@NoArgsConstructor
public class CheckedException extends RuntimeException {

    public CheckedException(String message) {
        super(message);
    }

    public CheckedException(Throwable cause) {
        super(cause);
    }

    public CheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
