package com.ht.authorization;

import lombok.Data;

import java.util.Map;

@Data
public class AccessTokenRequest {
    private String username;
    private String password;
    private Map<String, Object> ext; //其他请求参数
}
