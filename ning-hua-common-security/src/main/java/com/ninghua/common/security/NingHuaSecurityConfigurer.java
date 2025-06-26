package com.ninghua.common.security;

import cn.hutool.core.collection.CollectionUtil;
import com.ninghua.common.security.handler.AuthArgumentResolver;
import com.ninghua.common.security.interceptor.NingHuaSecurityInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Comparator;
import java.util.List;

/**
 * @Author Derek.Fung
 * @Date 2025/5/16 11:30
 **/
@Configuration
@Slf4j
@ComponentScan("com.ninghua.common.security.interceptor")
public class NingHuaSecurityConfigurer implements WebMvcConfigurer {

    @Autowired(required = false)
    private List<NingHuaSecurityInterceptor> interceptorList;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver());
    }

    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry) {
        if (CollectionUtil.isNotEmpty(this.interceptorList)) {
            this.interceptorList.stream()
                    .sorted(Comparator.comparing(NingHuaSecurityInterceptor::sort))
                    .forEach(interceptor -> {
                        if (CollectionUtil.isNotEmpty(interceptor.getPathPattern())) {
                            registry.addInterceptor(interceptor).addPathPatterns(interceptor.getPathPattern());
                        } else {
                            registry.addInterceptor(interceptor);
                        }
                    });
        }
    }
}
