package com.ninghua.common.database.config;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ninghua.common.core.constants.CommonConstants;
import com.ninghua.common.core.constants.SecurityConstants;
import com.ninghua.common.core.util.AuthPayload;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.request.RequestContextHolder;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

/**
 * @Author Derek.Fung
 * @Date 2025/4/30 10:30
 **/
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    @Autowired
    private HttpServletRequest request;
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();

        fillValIfNullByName("createTime", now, metaObject, true);
        fillValIfNullByName("updateTime", now, metaObject, true);
        fillValIfNullByName("createBy", getUserName(), metaObject, true);
        fillValIfNullByName("updateBy", getUserName(), metaObject, true);

        // 删除标记自动填充
        fillValIfNullByName("delFlag", CommonConstants.STATUS_NORMAL, metaObject, true);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        fillValIfNullByName("updateTime", LocalDateTime.now(), metaObject, true);
        fillValIfNullByName("updateBy", getUserName(), metaObject, true);
    }

    /**
     * 填充值，先判断是否有手动设置，优先手动设置的值，例如：job必须手动设置
     * @param fieldName 属性名
     * @param fieldVal 属性值
     * @param metaObject MetaObject
     * @param isCover 是否覆盖原有值,避免更新操作手动入参
     */
    private static void fillValIfNullByName(String fieldName, Object fieldVal, MetaObject metaObject, boolean isCover) {
        // 0. 如果填充值为空
        if (fieldVal == null) {
            return;
        }

        // 1. 没有 set 方法
        if (!metaObject.hasSetter(fieldName)) {
            return;
        }
        // 2. 如果用户有手动设置的值
        Object userSetValue = metaObject.getValue(fieldName);
        String setValueStr = StrUtil.str(userSetValue, Charset.defaultCharset());
        if (StrUtil.isNotBlank(setValueStr) && !isCover) {
            return;
        }
        // 3. field 类型相同时设置
        Class<?> getterType = metaObject.getGetterType(fieldName);
        if (ClassUtils.isAssignableValue(getterType, fieldVal)) {
            metaObject.setValue(fieldName, fieldVal);
        }
    }

    /**
     * 获取 spring security 当前的用户名
     * @return 当前用户名
     */
    private String getUserName() {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return null;
        }
        Object auth = this.request.getAttribute(SecurityConstants.AUTH_ATTR_NAME);
        AuthPayload payload = auth == null ? null : (AuthPayload) auth;
        return payload == null ? null : payload.getId();
    }
}
