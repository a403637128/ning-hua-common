package com.ninghua.common.lock;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @Author Derek.Fung
 * @Date 2025/6/24 13:54
 **/
public interface LockKeyBuilder {


    /**
     * 构建key
     *
     * @param invocation     invocation
     * @param definitionKeys 定义
     * @return key
     */
    String buildKey(MethodInvocation invocation, String[] definitionKeys);
}
