package com.ninghua.common.log;

import com.ninghua.common.log.interceptor.TraceInterceptor;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author Derek.Fung
 * @Date 2025/6/20 17:15
 **/
@Configuration
public class NingHuaLogConfigurer implements WebMvcConfigurer {

    @Resource
    private TraceInterceptor traceInterceptor;

    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry) {
        registry.addInterceptor(traceInterceptor).addPathPatterns("/**").order(-1);
    }
}
