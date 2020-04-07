package com.ht.oauth2.service.permission;

import com.ht.oauth2.repository.OAuth2PermissionRepository;
import com.ht.oauth2.model.OAuth2Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class OAuth2PermissionServiceImpl implements OAuth2PermissionService {
    @Autowired
    private OAuth2PermissionRepository oAuth2PermissionRepository;

    @Override
    public List<OAuth2Permission> findByScopeIn(Set<String> scopes) {
        return oAuth2PermissionRepository.findByScopeIn(scopes);
    }

    @Override
    public List<OAuth2Permission> findAll() {
        return oAuth2PermissionRepository.findAll();
    }
}
