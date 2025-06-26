package com.ninghua.common.security.annotation;

import java.lang.annotation.*;

/**
 * @Author Derek.Fung
 * @Date 2025/5/22 16:25
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreAppHeader {
}
