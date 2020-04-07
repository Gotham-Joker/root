package com.ht.oauth2.server;

import com.ht.oauth2.model.OAuth2Permission;
import lombok.Data;

@Data
public class Oauth2PermissionVO {
    private String id;
    private String name;

    public Oauth2PermissionVO(OAuth2Permission permission) {
        this.id = permission.getId();
        this.name = permission.getName();
    }
}
