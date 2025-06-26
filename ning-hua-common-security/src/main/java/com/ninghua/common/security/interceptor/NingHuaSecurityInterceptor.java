package com.ninghua.common.security.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

/**
 * @Author Derek.Fung
 * @Date 2025/5/16 11:49
 **/
public interface NingHuaSecurityInterceptor extends HandlerInterceptor {

    default List<String> getPathPattern() {
        return null;
    }

    default int sort() { return 1; }
}
