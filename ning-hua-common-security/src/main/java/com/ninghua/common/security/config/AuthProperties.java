package com.ninghua.common.security.config;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @Author Derek.Fung
 * @Date 2025/5/22 10:26
 **/
@Data
public class AuthProperties {

    private Boolean enable = false;

    private List<String> pathPattern = Collections.singletonList("/**");
}
