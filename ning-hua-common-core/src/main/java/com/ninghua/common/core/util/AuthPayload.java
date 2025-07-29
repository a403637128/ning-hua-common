package com.ninghua.common.core.util;

import com.ninghua.common.core.enums.AppClient;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

/**
 * @Author Derek.Fung
 * @Date 2025/5/16 14:27
 **/
@Data
public class AuthPayload {

    @Parameter(hidden = true)
    private AppClient client;

    @Parameter(hidden = true)
    private String id;

    @Parameter(hidden = true)
    private String extra;
}
