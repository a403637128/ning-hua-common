package com.ninghua.common.core.exception;

import com.ninghua.common.core.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author Derek.Fung
 * @Date 2025/5/16 14:30
 **/
@Data
@EqualsAndHashCode(callSuper=false)
public class BizException extends RuntimeException{

    private String message;

    private int code;

    private ErrorCode errorCode;


    public BizException(ErrorCode code) {
        super(code.getMessage());
        this.message = code.getMessage();
        this.code = code.getCode();
        this.errorCode = code;
    }

    public BizException(String message, ErrorCode code) {
        super(message);
        this.message = message;
        this.code = code.getCode();
        this.errorCode = code;
    }

    public BizException(String message) {
        super(message);
        this.message = message;
        this.code = ErrorCode.NORMAL_ERROR.getCode();
        this.errorCode = ErrorCode.NORMAL_ERROR;
    }
}
