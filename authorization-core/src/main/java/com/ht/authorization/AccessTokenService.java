package com.ht.authorization;

import com.ht.authorization.exception.AuthenticationException;

public interface AccessTokenService {

    /**
     * 请求accessToken
     * @param request
     * @return
     */
    public AccessTokenResponse createAccessToken(AccessTokenRequest request);

    /**
     * 把token转换成认证对象
     * @param accessToken
     * @return
     * @throws AuthenticationException
     */
    public Authentication parseToken(String accessToken) throws AuthenticationException;

}
