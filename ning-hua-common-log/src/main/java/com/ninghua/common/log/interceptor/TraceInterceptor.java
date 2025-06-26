package com.ninghua.common.log.interceptor;

import com.ninghua.common.core.util.RequestUtils;
import com.plumelog.core.TraceId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @Author Derek.Fung
 * @Date 2025/6/19 17:16
 **/
@Component
public class TraceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //String traceId = TraceContext.traceId();//核心是此处获取skywalking的traceId

        String requestId = RequestUtils.getGatewayRequestId(request);
        TraceId.logTraceID.set(requestId);
        return true;
    }
}
