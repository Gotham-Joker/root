package com.ht.oauth2.service.code;

import com.ht.authorization.Authentication;
import com.ht.oauth2.OAuth2AuthenticationRequest;
import com.ht.oauth2.model.OAuth2AuthorizationCode;

public interface OAuth2AuthorizationCodeService {

    /**
     * 用户同意授权之后封装授权的信息，然后生成一个code
     * @return
     */
    String createAuthorizationCode(Authentication authentication, OAuth2AuthenticationRequest request);

    /**
     * 消费code
     * @param code
     * @return
     */
    OAuth2AuthorizationCode consumeAuthorizationCode(String code);
}
