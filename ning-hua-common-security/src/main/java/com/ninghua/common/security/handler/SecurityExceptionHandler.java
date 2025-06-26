package com.ninghua.common.security.handler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.ninghua.common.core.enums.ErrorCode;
import com.ninghua.common.core.result.NingHuaResult;
import com.ninghua.common.core.util.LogUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author Derek.Fung
 * @Date 2025/5/26 10:47
 **/
@Order(1)
@Slf4j
@ControllerAdvice
public class SecurityExceptionHandler {

    @ResponseBody
    @ExceptionHandler(NotLoginException.class)
    public NingHuaResult<String> notLoginExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        LogUtils.buildStatus(request, response, ex);
        log.error("错误信息"+ex.toString());
        return NingHuaResult.failed(ErrorCode.TOKEN_WRONG);
    }

    @ResponseBody
    @ExceptionHandler(NotPermissionException.class)
    public NingHuaResult<String> notPermissionExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        LogUtils.buildStatus(request, response, ex);
        log.error("错误信息"+ex.toString());
        return NingHuaResult.failed(ErrorCode.NOT_PERMISSION);
    }

}
