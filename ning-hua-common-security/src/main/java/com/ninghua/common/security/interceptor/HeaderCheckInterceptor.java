package com.ninghua.common.security.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ninghua.common.core.enums.ErrorCode;
import com.ninghua.common.core.exception.BizException;
import com.ninghua.common.core.request.NingHuaHeader;
import com.ninghua.common.core.util.RequestUtils;
import com.ninghua.common.security.annotation.IgnoreAppHeader;
import com.ninghua.common.security.config.SecurityProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Author Derek.Fung
 * @Date 2025/5/22 16:20
 **/
@Slf4j
@AllArgsConstructor
public class HeaderCheckInterceptor implements NingHuaSecurityInterceptor{

    private final SecurityProperties securityProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        IgnoreAppHeader ignoreAppHeader = handlerMethod.getMethodAnnotation(IgnoreAppHeader.class);
        if (ignoreAppHeader != null) {
            return true;
        }
        NingHuaHeader ningHuaHeader = RequestUtils.getNingHuaHeaderByRequest(request);
        this.validate(ningHuaHeader, "platform", "version", "client", "system");
        return true;
    }

    private void validate(NingHuaHeader appHeader, String... requiredNames) {
        JSONObject obj = JSONUtil.parseObj(appHeader);
        Optional<String> optional = Arrays.stream(requiredNames).filter(e -> !obj.containsKey(e)).findFirst();
        if (optional.isPresent()) {
            log.error("头信息{}不能为空", StrUtil.lowerFirst(NingHuaHeader.PREFIX + StrUtil.toSymbolCase(optional.get(), '-')));
            throw new BizException(ErrorCode.ILLEGAL_HEADER);
        }
    }

    @Override
    public List<String> getPathPattern() {
        return this.securityProperties.getAuth().getPathPattern();
    }
}
