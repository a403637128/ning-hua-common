package com.ninghua.common.security.annotation;

import java.lang.annotation.*;

/**
 * @Author Derek.Fung
 * @Date 2025/5/16 15:49
 **/
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auth {
}
