package com.ninghua.common.core.util;

import com.ninghua.common.core.request.NingHuaHeader;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Derek.Fung
 * @Date 2025/5/26 10:57
 **/
@UtilityClass
@Slf4j
public class LogUtils {

    public void buildStatus(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        String path = request.getServletPath();
        NingHuaHeader head = RequestUtils.getNingHuaHeaderByRequest(request);
        String body = "";
        try {
            body = RequestUtils.getRequestJsonStr(request);
        } catch (Exception e) {

        }
        String info = "path:" + path + "\n"
                + "head=" + head + "\n"
                + "body=" + body;
        log.error(info, ex);

    }
}
