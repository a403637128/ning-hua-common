package com.ninghua.common.log.filter;

import cn.hutool.json.JSONUtil;
import com.ninghua.common.core.request.RepeatBodyRequestWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @Author Derek.Fung
 * @Date 2025/6/23 15:04
 **/
@Component
public class RequestLogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request instanceof RepeatBodyRequestWrapper httpRequest) {
            System.out.println(JSONUtil.toJsonStr(httpRequest.getParameterMap()));
            filterChain.doFilter(request, response);
        }else{
            filterChain.doFilter(request, response);
        }
    }
}
