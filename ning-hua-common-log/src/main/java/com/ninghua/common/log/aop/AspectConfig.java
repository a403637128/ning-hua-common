package com.ninghua.common.log.aop;

import com.plumelog.trace.aspect.AbstractAspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @Author Derek.Fung
 * @Date 2025/6/20 16:54
 **/
@Aspect
@Component
public class AspectConfig extends AbstractAspect {

    @Around(value = "within(com.ninghua.*.portal.controller..*)")//这边写自己的包的路径
    public Object around(JoinPoint joinPoint) throws Throwable {
        return aroundExecute(joinPoint);
    }
}
