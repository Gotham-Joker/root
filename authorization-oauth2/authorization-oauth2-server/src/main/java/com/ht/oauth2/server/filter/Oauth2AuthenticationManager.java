package com.ht.oauth2.server.filter;

import com.ht.authorization.Authentication;
import com.ht.authorization.BearerTokenExtractor;
import com.ht.authorization.exception.AuthenticationException;
import com.ht.authorization.filter.AuthenticationManager;
import com.ht.oauth2.service.token.AuthorizationServerTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class Oauth2AuthenticationManager implements AuthenticationManager {
    private AuthorizationServerTokenService authorizationServerTokenService;

    public Oauth2AuthenticationManager(AuthorizationServerTokenService authorizationServerTokenService) {
        Assert.notNull(authorizationServerTokenService, "authorizationServerTokenService");
        this.authorizationServerTokenService = authorizationServerTokenService;
    }

    /**
     * 从header中获取token，并根据token获取Authentication
     *
     * @param request
     * @param response
     * @throws AuthenticationException 如果登录失败，就会抛出这个异常
     */
    public Authentication authenticate(HttpServletRequest request, HttpServletResponse response) {

        // 从请求参数中获取
        String accessToken = request.getParameter("accessToken");
        if (StringUtils.hasText(accessToken)) {
            return authorizationServerTokenService.parseAccessToken(accessToken);
        }

        String bearerToken = BearerTokenExtractor.extractToken(request);
        return authorizationServerTokenService.parseAccessToken(bearerToken);
    }
}
