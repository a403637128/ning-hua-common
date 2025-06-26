package com.ninghua.common.database.config;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.core.Ordered;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * 清空上文的DS 设置避免污染当前线程
 * @Author Derek.Fung
 * @Date 2025/5/29 16:31
 **/
public class ClearTtlDataSourceFilter extends GenericFilterBean implements Ordered {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        DynamicDataSourceContextHolder.clear();
        filterChain.doFilter(servletRequest, servletResponse);
        DynamicDataSourceContextHolder.clear();
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
