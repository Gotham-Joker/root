package com.ht.authorization.filter;

import com.ht.authorization.AccessTokenService;
import com.ht.authorization.Authentication;
import com.ht.authorization.BearerTokenExtractor;
import com.ht.authorization.exception.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class DefaultAuthenticationManager implements AuthenticationManager {
    private AccessTokenService accessTokenService;

    public DefaultAuthenticationManager(AccessTokenService accessTokenService) {
        Assert.notNull(accessTokenService, "accessTokenService不能为空");
        this.accessTokenService = accessTokenService;
    }

    /**
     * 从header中获取token，并根据token获取Authentication
     *
     * @param request
     * @param response
     */
    public Authentication authenticate(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // 从请求参数中获取
        String accessToken = request.getParameter("accessToken");
        if (StringUtils.hasText(accessToken)) {
            return accessTokenService.parseToken(accessToken);
        }

        String bearerToken = BearerTokenExtractor.extractToken(request);
        return accessTokenService.parseToken(bearerToken);
    }
}
