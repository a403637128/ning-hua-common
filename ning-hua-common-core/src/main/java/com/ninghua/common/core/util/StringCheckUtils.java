package com.ninghua.common.core.util;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

/**
 * @Author Derek.Fung
 * @Date 2025/5/22 11:58
 **/
@UtilityClass
public class StringCheckUtils {

    /**
     * 判断字符串是否包含非允许的字符（只允许数字、字母、中文）
     * @param str 待检查的字符串
     * @return true-包含非允许字符，false-不包含
     */
    public boolean hasInvalidChar(String str) {
        if (StrUtil.isBlank(str)) {
            return false;
        }

        // 正则：0-200位，仅包含字母、数字、下划线
        return !str.matches("^[a-zA-Z0-9_]{0,200}$");
    }
}
