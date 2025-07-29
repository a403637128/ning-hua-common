package com.ninghua.common.core.filter;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import com.ninghua.common.core.constants.CommonConstants;
import com.ninghua.common.core.request.RepeatBodyRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @Author Derek.Fung
 * @Date 2025/5/21 18:13
 **/
@Slf4j(topic = "paramLogger")
@WebFilter(urlPatterns = "/*")
public class RequestWrapperFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(servletRequest.getContentType() != null && servletRequest.getContentType().contains("multipart/form-data")){
            filterChain.doFilter(servletRequest, servletResponse);
        } else if (servletRequest instanceof HttpServletRequest httpRequest) {
            RepeatBodyRequestWrapper requestWrapper = new RepeatBodyRequestWrapper(httpRequest);
            //记录请求信息
            String path = requestWrapper.getServletPath();
            String param = JSONUtil.toJsonStr(requestWrapper.getParameterMap());
            String body = IoUtil.read(requestWrapper.getInputStream(), StandardCharsets.UTF_8);
            String source = requestWrapper.getHeader("source");
            String version = requestWrapper.getHeader("version");
            log.info("request detail\n"
                    + "requestId=" + requestWrapper.getHeader(CommonConstants.REQUEST_ID) + "\n"
                    + "ip=" + requestWrapper.getHeader(CommonConstants.USER_IP) + "\n"
                    + "forceUpdate(source=" + source + ", version=" + version + ")\n"
                    + "path=" + path + "\n"
                    + "param=" + param + "\n"
                    + "body=" + body);
            filterChain.doFilter(requestWrapper, servletResponse);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
