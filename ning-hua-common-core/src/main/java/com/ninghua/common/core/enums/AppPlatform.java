package com.ninghua.common.core.enums;

import java.util.Arrays;

/**
 * @Author Derek.Fung
 * @Date 2025/5/15 16:32
 **/
public enum AppPlatform {

    /* ios */
    IOS,
    /* android */
    ANDROID,
    /* 微信小程序 */
    WX_MA,
    /* pc网页 */
    WEB,
    /* 移动网页 */
    WAP,
    /* 支付宝小程序 */
    ALI_MA;

    /**
     * 是否App端
     */
    public boolean isApp() {
        return Arrays.asList(AppPlatform.ANDROID, AppPlatform.IOS).contains(this);
    }

    /**
     * 是否微信小程序端
     */
    public boolean isWxMa() {
        return AppPlatform.WX_MA.equals(this);
    }
}
