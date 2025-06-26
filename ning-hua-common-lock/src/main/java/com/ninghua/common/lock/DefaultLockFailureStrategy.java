package com.ninghua.common.lock;

import com.ninghua.common.core.exception.LockFailureException;

import java.lang.reflect.Method;

/**
 * @Author Derek.Fung
 * @Date 2025/6/24 18:17
 **/
public class DefaultLockFailureStrategy implements LockFailureStrategy {

    protected static String DEFAULT_MESSAGE = "request failed,please retry it.";

    @Override
    public void onLockFailure(String key, Method method, Object[] arguments) {
        throw new LockFailureException(DEFAULT_MESSAGE);
    }
}
