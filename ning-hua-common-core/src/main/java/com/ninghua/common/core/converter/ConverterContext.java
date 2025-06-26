package com.ninghua.common.core.converter;

import com.ninghua.common.core.request.NingHuaHeader;
import com.ninghua.common.core.util.AuthPayload;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Derek.Fung
 * @Date 2025/5/15 17:06
 **/
@Data
public class ConverterContext {

    private AuthPayload payload;

    private Map<String, Object> extras = new HashMap<>();

    private NingHuaHeader appHeader;

    private AbstractResConverterHandler converter;

    public boolean isAuth() {
        return this.payload != null;
    }
}
