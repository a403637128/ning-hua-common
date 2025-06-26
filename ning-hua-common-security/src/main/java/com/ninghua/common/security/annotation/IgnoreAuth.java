package com.ninghua.common.security.annotation;

import java.lang.annotation.*;

/**
 * 忽略认证注解
 * @Author Derek.Fung
 * @Date 2025/4/30 15:31
 **/
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreAuth {

    /**
     * 是否需要鉴权
     * @return false, true
     */
    boolean value() default true;

    /**
     * 需要特殊判空的字段(预留)
     * @return {}
     */
    String[] field() default {};
}
