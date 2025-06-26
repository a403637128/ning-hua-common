package com.ninghua.common.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author Derek.Fung
 * @Date 2025/5/21 18:17
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class FeignBadRequestException extends RuntimeException {

    private String methodKey;

    /**
     * 是否敏感级别的异常，敏感级别的异常则不会显示给前端
     */
    private boolean isError;

    public FeignBadRequestException(String methodKey, String message) {
        this(methodKey, message, false);
    }

    public FeignBadRequestException(String methodKey, String message, boolean isError) {
        super(message);
        this.isError = isError;
        this.methodKey = methodKey;
    }
}
