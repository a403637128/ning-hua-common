package com.ninghua.common.feign.config;

import cn.hutool.json.JSONUtil;
import com.ninghua.common.core.enums.ErrorCode;
import com.ninghua.common.core.exception.BizException;
import com.ninghua.common.core.result.NingHuaResult;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.Util;
import feign.codec.ErrorDecoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

/**
 * @Author Derek.Fung
 * @Date 2025/5/22 09:30
 **/

@Slf4j
@Configuration
@EnableFeignClients(basePackages = {"com.ninghua.*.api.feign"})
public class FeignConf {

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            try {
                String body = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
                log.error("Feign Error : " + methodKey + body);
                NingHuaResult result = null;
                ErrorCode code = null;
                if(JSONUtil.isTypeJSON(body)){
                    result = JSONUtil.toBean(body, NingHuaResult.class);
                    code = ErrorCode.getByCode(result.getError().getCode());
                }else{
                    result = NingHuaResult.failed(ErrorCode.SYSTEM_ERROR);
                }
                return new BizException(result.getError().getDescription(), code == null ? ErrorCode.SYSTEM_ERROR : code);
            } catch (IOException e) {
                log.error("Feign IOException ", e);
                return new BizException(ErrorCode.SYSTEM_ERROR);
            }
        };
    }
    @Bean
    public Retryer feignRetryer(){
        return new Retryer.Default(100, 1000, 4);
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                // 转发头部信息
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        if (!"content-length".equals(name)) {
                            String values = request.getHeader(name);
                            requestTemplate.header(name, values);
                        }
                    }
                }
            }
        };
    }
}
