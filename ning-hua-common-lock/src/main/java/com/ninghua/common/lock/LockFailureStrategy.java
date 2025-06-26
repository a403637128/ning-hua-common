package com.ninghua.common.lock;

import java.lang.reflect.Method;

/**
 * 当加锁失败时的处理策略
 * @Author Derek.Fung
 * @Date 2025/6/24 13:54
 **/
public interface LockFailureStrategy {

    /**
     * 当加锁失败时的处理策略
     *
     * @param key 用于获取锁的key
     * @param method 方法
     * @param arguments 方法参数
     * @throws Exception 处理过程中可能抛出的异常，如果抛出异常则会终止方法执行
     */
    void onLockFailure(String key, Method method, Object[] arguments) throws Exception;

}
