package com.ht.oauth2.service.permission;

import com.ht.authorization.PermissionService;
import com.ht.oauth2.model.OAuth2Permission;

import java.util.List;
import java.util.Set;

public interface OAuth2PermissionService extends PermissionService {
    public List<OAuth2Permission> findByScopeIn(Set<String> scopes);
}
