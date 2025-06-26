package com.ninghua.common.core.util;

import com.ninghua.common.core.enums.AppClient;
import lombok.Data;

/**
 * @Author Derek.Fung
 * @Date 2025/5/16 14:27
 **/
@Data
public class AuthPayload {

    private AppClient client;

    private String id;

    private String extra;
}
