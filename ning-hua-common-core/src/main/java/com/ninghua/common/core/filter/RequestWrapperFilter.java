package com.ninghua.common.core.filter;

import com.ninghua.common.core.request.RepeatBodyRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * @Author Derek.Fung
 * @Date 2025/5/21 18:13
 **/
@WebFilter(urlPatterns = "/*")
public class RequestWrapperFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest httpRequest) {
            RepeatBodyRequestWrapper requestWrapper = new RepeatBodyRequestWrapper(httpRequest);
            filterChain.doFilter(requestWrapper, servletResponse);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
