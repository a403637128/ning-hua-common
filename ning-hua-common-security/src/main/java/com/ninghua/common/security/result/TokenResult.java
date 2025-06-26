package com.ninghua.common.security.result;

import lombok.Data;

/**
 * @Author Derek.Fung
 * @Date 2025/4/30 15:37
 **/
@Data
public class TokenResult {

    private String id;

    private String userId;

    private String clientId;

    private String username;

    private String accessToken;

    private String issuedAt;

    private String expiresAt;


}
