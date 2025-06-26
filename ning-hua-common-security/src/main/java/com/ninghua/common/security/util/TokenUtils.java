package com.ninghua.common.security.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Derek.Fung
 * @Date 2025/5/28 16:00
 **/
@UtilityClass
public class TokenUtils {

    private final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-:._~+/]+=*)$",
            Pattern.CASE_INSENSITIVE);
    /**
     * 获取 Authorization 请求头的token值
     *  格式应为 Bearer {token}
     */
    public String resolveFromAuthorization(String authorization) {
        if (StringUtils.startsWithIgnoreCase(authorization, "bearer")) {
            Matcher matcher = authorizationPattern.matcher(authorization);
            if (matcher.matches()) {
                return matcher.group("token");
            }
        }
        return authorization;
    }
}
