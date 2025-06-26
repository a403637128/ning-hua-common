package com.ninghua.common.core.request;

import com.ninghua.common.core.enums.AppClient;
import com.ninghua.common.core.enums.AppPlatform;
import lombok.Data;

/**
 * @Author Derek.Fung
 * @Date 2025/5/15 16:31
 **/
@Data
public class NingHuaHeader {

    public static final String PREFIX = "App-";

    /**
     * 客户端平台
     */
    private AppPlatform platform;

    /**
     * API消费者端
     */
    private AppClient client;

    /**
     * 客户端APP版本
     */
    private String version;

    /**
     * 客户端系统信息
     */
    private String system;

    /**
     * 客户端设备号
     */
    private String deviceId;


    /**
     * 客户端当前经度，经纬度默认取天安门
     */
    private String lat = "39.90";

    /**
     * 客户端当前纬度，经纬度默认取天安门
     */
    private String lng = "116.38";

    /**
     * 区域码 默认全国
     */
    private String areaCode = "0";

}
