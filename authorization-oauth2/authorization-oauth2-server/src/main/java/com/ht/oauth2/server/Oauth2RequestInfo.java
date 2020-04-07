package com.ht.oauth2.server;

import lombok.Data;

import java.util.List;

@Data
public class Oauth2RequestInfo {
    private String clientName;
    private List<Oauth2PermissionVO> scopes;
}
