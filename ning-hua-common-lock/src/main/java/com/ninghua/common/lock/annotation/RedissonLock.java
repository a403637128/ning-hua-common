package com.ninghua.common.lock.annotation;

import com.ninghua.common.lock.executor.RedissonLockExecutor;

import java.lang.annotation.*;

/**
 * @Author Derek.Fung
 * @Date 2025/6/24 11:58
 **/
@Lock4j(executor = RedissonLockExecutor.class)
@Target(value = {ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RedissonLock {
}
