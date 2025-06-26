package com.ninghua.common.security.interceptor;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.ninghua.common.core.constants.CommonConstants;
import com.ninghua.common.core.constants.SecurityConstants;
import com.ninghua.common.core.enums.AppClient;
import com.ninghua.common.core.enums.ErrorCode;
import com.ninghua.common.core.exception.BizException;
import com.ninghua.common.core.request.NingHuaHeader;
import com.ninghua.common.core.util.AuthPayload;
import com.ninghua.common.core.util.RequestUtils;
import com.ninghua.common.security.annotation.IgnoreAuth;
import com.ninghua.common.security.config.AuthProperties;
import com.ninghua.common.security.config.SecurityProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

import java.util.List;

/**
 * 权限拦截器
 * 由网关统一解析token
 * @Author Derek.Fung
 * @Date 2025/5/16 11:52
 **/
@AllArgsConstructor
public class AuthenticationInterceptor implements NingHuaSecurityInterceptor{

    private final SecurityProperties securityProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        IgnoreAuth requiredAuth = handlerMethod.getMethodAnnotation(IgnoreAuth.class);

        if (requiredAuth != null && !requiredAuth.value()) {
            return true;
        }
        String profileId = request.getHeader(SecurityConstants.PROFILE_ID);
        String client = request.getHeader(SecurityConstants.EXTRA_CLIENT);
        if(StrUtil.isBlank(profileId) || StrUtil.isBlank(client)) {
            throw new BizException(ErrorCode.TOKEN_WRONG);
        }
        NingHuaHeader ningHuaHeader = RequestUtils.getNingHuaHeaderByRequest(request);
        if (!ningHuaHeader.getClient().name().equals(client)) {
            throw new BizException("token不能跨端使用", ErrorCode.TOKEN_WRONG);
        }
        AuthPayload auth = new AuthPayload();
        auth.setClient(Enum.valueOf(AppClient.class, client));
        auth.setId(profileId);
        request.setAttribute(SecurityConstants.AUTH_ATTR_NAME, auth);
        return true;
    }

    @Override
    public List<String> getPathPattern() {
        return this.securityProperties.getAuth().getPathPattern();
    }
}
