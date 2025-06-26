package com.ninghua.common.core.exception;

/**
 * @Author Derek.Fung
 * @Date 2025/6/24 14:29
 **/
public class LockException extends RuntimeException {

    public LockException() {
        super();
    }

    public LockException(String message) {

        super(message);
    }
}
