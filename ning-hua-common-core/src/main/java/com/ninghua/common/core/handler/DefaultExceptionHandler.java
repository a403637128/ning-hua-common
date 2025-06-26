package com.ninghua.common.core.handler;

import cn.hutool.core.util.StrUtil;
import com.ninghua.common.core.enums.ErrorCode;
import com.ninghua.common.core.exception.BizException;
import com.ninghua.common.core.exception.LockFailureException;
import com.ninghua.common.core.result.NingHuaResult;
import com.ninghua.common.core.util.LogUtils;
import com.ninghua.common.core.util.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * @Author Derek.Fung
 * @Date 2025/5/16 16:19
 **/
@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler {

    @ResponseBody
    @ExceptionHandler(BizException.class)
    public NingHuaResult<String> bizExceptionHandler(HttpServletRequest request, HttpServletResponse response, BizException ex) {
        RequestUtils.dealIfFeignRequest(request, response);
        String requestId = RequestUtils.getGatewayRequestId(request);
        log.warn(StrUtil.format("{} | 请求异常: {}", requestId, ex.getMessage()), ex);
        return NingHuaResult.failed(ex.getMessage(), ex.getErrorCode());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public NingHuaResult<String> defaultExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        LogUtils.buildStatus(request, response, ex);
        log.error(ex.getMessage(), ex);
        return NingHuaResult.failed(ErrorCode.SYSTEM_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(LockFailureException.class)
    public NingHuaResult<String> lockFailureExceptionExceptionHandler(HttpServletRequest request, HttpServletResponse response, LockFailureException ex) {
        log.warn(ex.getMessage(), ex);
        return NingHuaResult.failed(ErrorCode.CONFLICT);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public NingHuaResult<String> methodArgumentNotValidExceptionHandler(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);
        return NingHuaResult.failed(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(), ErrorCode.NORMAL_ERROR);
    }

    @ExceptionHandler({ NoResourceFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public NingHuaResult<String> notFoundExceptionHandler(NoResourceFoundException exception) {
        log.error("请求路径 404 {}", exception.getMessage());
        return NingHuaResult.failed(ErrorCode.NOT_FOUND);
    }


}
