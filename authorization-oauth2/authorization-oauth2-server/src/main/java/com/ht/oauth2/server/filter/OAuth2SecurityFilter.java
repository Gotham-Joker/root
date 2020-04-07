package com.ht.oauth2.server.filter;

import com.ht.authorization.filter.AbstractSecurityFilter;
import com.ht.oauth2.service.permission.OAuth2PermissionService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class OAuth2SecurityFilter extends AbstractSecurityFilter {

    public OAuth2SecurityFilter(Oauth2AuthenticationManager authenticationManager, OAuth2PermissionService permissionService, List<String> permitUrls) {
        super(authenticationManager, permissionService, permitUrls);
    }

}
