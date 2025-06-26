package com.ninghua.common.core.converter;

import com.ninghua.common.core.constants.SecurityConstants;
import com.ninghua.common.core.util.AuthPayload;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @Author Derek.Fung
 * @Date 2025/5/28 14:17
 **/
@Lazy
@Component
public class ResConverterHandler extends AbstractResConverterHandler{

    @Autowired
    private HttpServletRequest request;

    @Override
    protected AuthPayload getAuth() {
        Object auth = this.request.getAttribute(SecurityConstants.AUTH_ATTR_NAME);
        return auth == null ? null : (AuthPayload) auth;
    }
}
