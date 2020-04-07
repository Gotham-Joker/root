package com.ht.oauth2;

import lombok.Data;

import java.util.Set;

@Data
public class OAuth2AccessTokenResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private Long expireIn;
    private String refreshToken;
    private Set<String> scope;

    public OAuth2AccessTokenResponse() {
    }

    public OAuth2AccessTokenResponse(String accessToken, String refreshToken,Long expireIn, Set<String> scope) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expireIn = expireIn;
        this.scope = scope;
    }
}
