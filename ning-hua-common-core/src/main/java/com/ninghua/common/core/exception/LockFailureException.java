package com.ninghua.common.core.exception;

import java.lang.reflect.Method;

/**
 * @Author Derek.Fung
 * @Date 2025/6/24 18:19
 **/
public class LockFailureException extends LockException{

    public LockFailureException() {

    }

    public LockFailureException(String message) {
        super(message);
    }
}
