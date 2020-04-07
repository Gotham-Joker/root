package com.ht.oauth2.repository;

import com.ht.oauth2.model.OAuth2AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorizationServerTokenRepository extends JpaRepository<OAuth2AccessToken, String> {

    OAuth2AccessToken findByAccessToken(String accessToken);

    OAuth2AccessToken findByRefreshToken(String refreshToken);

    OAuth2AccessToken findByClientIdAndUserId(String clientId,String userId);

    void deleteByClientIdAndUserId(String clientId,String userId);

    void deleteByRefreshToken(String refreshToken);
}
