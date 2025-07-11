package com.ninghua.common.core.constants;

/**
 * @Author Derek.Fung
 * @Date 2025/4/29 17:59
 **/
public interface SecurityConstants {

    String AUTHORIZATION = "authorization";

    String EXTRA_CLIENT = "token-client";

    String EXTRA_EXPIRES_MS = "token-expires";

    String AUTH_ATTR_NAME = "nh-auth";

    /**
     * profileId网关放入头部时的key
     */
    String PROFILE_ID = "Profile-Id";

    /**
     * 角色前缀
     */
    String ROLE = "ROLE_";



    /**
     * 内部
     */
    String FROM_IN = "Y";

    /**
     * 标志
     */
    String FROM = "from";

    /**
     * 默认登录URL
     */
    String OAUTH_TOKEN_URL = "/oauth2/token";

    /**
     * grant_type
     */
    String REFRESH_TOKEN = "refresh_token";

    /**
     * password 模式
     */
    String PASSWORD = "password";

    /**
     * 授权码
     */
    String AUTHORIZATION_CODE = "authorization_code";

    /**
     * 手机号登录
     */
    String MOBILE = "mobile";

    /**
     * {bcrypt} 加密的特征码
     */
    String BCRYPT = "{bcrypt}";

    /**
     * {noop} 加密的特征码
     */
    String NOOP = "{noop}";

    /**
     * 用户名
     */
    String USERNAME = "username";

    /**
     * 用户信息
     */
    String DETAILS_USER = "user_info";

    /**
     * 用户ID
     */
    String DETAILS_USER_ID = "user_id";

    /**
     * 协议字段
     */
    String DETAILS_LICENSE = "license";

    /**
     * 验证码有效期,默认 60秒
     */
    long CODE_TIME = 60;

    /**
     * 验证码长度
     */
    String CODE_SIZE = "6";

    /**
     * 客户端模式
     */
    String CLIENT_CREDENTIALS = "client_credentials";

    /**
     * 客户端ID
     */
    String CLIENT_ID = "clientId";

    /**
     * 短信登录 参数名称
     */
    String SMS_PARAMETER_NAME = "mobile";

    /**
     * 授权码模式confirm
     */
    String CUSTOM_CONSENT_PAGE_URI = "/oauth2/confirm_access";
}
