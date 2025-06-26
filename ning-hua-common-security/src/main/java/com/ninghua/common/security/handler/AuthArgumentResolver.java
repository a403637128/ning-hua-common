package com.ninghua.common.security.handler;

import com.ninghua.common.core.constants.SecurityConstants;
import com.ninghua.common.core.util.AuthPayload;
import com.ninghua.common.security.annotation.Auth;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @Author Derek.Fung
 * @Date 2025/5/16 15:48
 **/
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(Auth.class) && methodParameter.getParameterType().isAssignableFrom(AuthPayload.class);
    }

    @NotNull
    @Override
    public Object resolveArgument(@NotNull MethodParameter methodParameter, @NotNull ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, @NotNull WebDataBinderFactory webDataBinderFactory) throws Exception {
        return nativeWebRequest.getAttribute(SecurityConstants.AUTH_ATTR_NAME, RequestAttributes.SCOPE_REQUEST);
    }
}
