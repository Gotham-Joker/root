package com.ht.oauth2;

import com.ht.authorization.DefaultAuthentication;
import com.ht.oauth2.model.OAuth2Permission;

import java.util.HashMap;
import java.util.List;

public class Oauth2Authentication extends DefaultAuthentication {
    private String clientId;

    public Oauth2Authentication(String id, String clientId, List<OAuth2Permission> permissions) {
        super(id, permissions, new HashMap<>());
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

}
