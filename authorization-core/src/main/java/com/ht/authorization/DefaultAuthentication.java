package com.ht.authorization;

import lombok.Getter;

import java.util.List;
import java.util.Map;

public class DefaultAuthentication implements Authentication {
    @Getter
    private String id;
    @Getter
    private List<? extends Permission> permissions;

    private Map<String, Object> attributes;

    public DefaultAuthentication(String id, List<? extends Permission> permissions,Map<String,Object> attributes) {
        this.id = id;
        this.permissions = permissions;
        this.attributes = attributes;
    }

    @Override
    public <T> T getAttribute(String key) {
        return (T) attributes.get(key);
    }

}
