package com.ninghua.common.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author Derek.Fung
 * @Date 2025/5/22 10:28
 **/
@Data
@Component
@ConfigurationProperties("ninghua.security")
public class SecurityProperties {

    private AuthProperties auth = new AuthProperties();
}
