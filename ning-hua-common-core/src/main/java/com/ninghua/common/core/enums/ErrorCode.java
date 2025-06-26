package com.ninghua.common.core.enums;

/**
 * @Author Derek.Fung
 * @Date 2025/5/16 14:40
 **/
public enum ErrorCode {

    SUCCESS(0, "成功"),
    TOKEN_WRONG(100000, "token非法"),
    NORMAL_ERROR(100400, "系统升级中"),

    NOT_PERMISSION(403, "没有权限"),

    ILLEGAL_HEADER(417, "请求头信息错误！"),

    SYSTEM_ERROR(500, "系统升级中,请稍后再试！"),

    MANY_REQUESTS(429, "操作频繁,请稍后再试！"),

    CONFLICT(409, "操作正在处理中,请稍后再试！"),

    NOT_FOUND(404, "请求路径 404！"),;

    private int code;

    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ErrorCode getByCode(int code) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.code == code) {
                return errorCode;
            }
        }
        return null;
    }
}
