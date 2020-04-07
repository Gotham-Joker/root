package com.ht.oauth2.service.token;

import com.ht.oauth2.Oauth2Authentication;
import com.ht.oauth2.model.OAuth2AccessToken;
import com.ht.oauth2.model.OAuth2AuthorizationCode;

/**
 * token服务
 */
public interface AuthorizationServerTokenService {

    /**
     * 创建access_token
     * @return
     */
    OAuth2AccessToken createAccessToken(OAuth2AuthorizationCode authorizationCode);

    /**
     * 刷新access_token
     * @return
     */
    OAuth2AccessToken refreshAccessToken(String refreshToken);

    /**
     * 获取已有的access_token
     * @return
     */
    Oauth2Authentication parseAccessToken(String accessToken);

}
