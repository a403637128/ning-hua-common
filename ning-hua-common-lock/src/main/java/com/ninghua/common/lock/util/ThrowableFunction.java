package com.ninghua.common.lock.util;

/**
 * @Author Derek.Fung
 * @Date 2025/6/24 17:32
 **/
public interface ThrowableFunction<T, R> {

    R apply(T t) throws Throwable;
}
