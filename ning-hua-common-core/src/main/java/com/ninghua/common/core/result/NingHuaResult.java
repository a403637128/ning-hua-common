package com.ninghua.common.core.result;

import com.ninghua.common.core.constants.CommonConstants;
import com.ninghua.common.core.enums.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Derek.Fung
 * @Date 2025/4/30 14:03
 **/
@Data
@Schema(description = "统一返回")
public class NingHuaResult<T> implements Serializable {

    @Schema(description = "是否成功")
    private boolean succeed;

    @Schema(description = "返回数据")
    private T data;

    @Schema(description = "返回信息")
    private String msg;

    @Schema(description = "错误信息")
    private Error error;

    @Data
    @Schema(description = "错误信息")
    public static class Error {

        @Schema(description = "错误码")
        private Integer code;

        @Schema(description = "错误描述")
        private String description;

        @Schema(description = "请求ID")
        private String requestId;
    }

    public static <T> NingHuaResult<T> ok(){
        return ok(null, null);
    }
    public static <T> NingHuaResult<T> ok(T data){
        return ok(data, null);
    }
    public static <T> NingHuaResult<T> failed(ErrorCode errorCode){
        return failed(errorCode.getMessage(), errorCode);
    }
    public static <T> NingHuaResult<T> failed(String description){
        return failed(description, ErrorCode.NORMAL_ERROR);
    }

    public static <T> NingHuaResult<T> ok(T data, Object extend){
        NingHuaResult<T> result = new NingHuaResult<>();
        result.setSucceed(true);
        result.setData(data);
        result.setError(null);
        result.setMsg("成功");
        return result;
    }

    public static <T>  NingHuaResult<T>  failed(String description, ErrorCode errorCode){
        NingHuaResult<T> result = new NingHuaResult<>();
        result.setSucceed(false);
        result.setData(null);
        result.setMsg("失败");
        Error error= new Error();
        error.setCode(errorCode.getCode());
        error.setDescription(description);
        result.setError(error);
        return result;
    }

}
