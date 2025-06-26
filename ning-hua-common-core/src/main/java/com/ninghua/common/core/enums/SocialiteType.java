package com.ninghua.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author Derek.Fung
 * @Date 2025/5/20 14:20
 **/
@AllArgsConstructor
public enum SocialiteType {

    /* 微信APP */
    WX_APP("微信APP"),
    /* 微信公众号 */
    WX_MP("微信公众号"),
    /* 微信小程序 */
    WX_MA("微信小程序"),
    /* 支付宝小程序 */
    ALI_MA("支付宝小程序"),

    APPLE("苹果登录");

    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
