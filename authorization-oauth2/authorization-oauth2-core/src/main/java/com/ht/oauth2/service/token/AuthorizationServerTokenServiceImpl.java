package com.ht.oauth2.service.token;

import com.ht.oauth2.Oauth2Authentication;
import com.ht.oauth2.exceptions.InvalidGrantException;
import com.ht.oauth2.exceptions.OAuth2Exception;
import com.ht.oauth2.model.OAuth2AccessToken;
import com.ht.oauth2.model.OAuth2AuthorizationCode;
import com.ht.oauth2.model.OAuth2Permission;
import com.ht.oauth2.repository.AuthorizationServerTokenRepository;
import com.ht.oauth2.repository.OAuth2PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(noRollbackFor = OAuth2Exception.class)
public class AuthorizationServerTokenServiceImpl implements AuthorizationServerTokenService {
    @Autowired
    private AuthorizationServerTokenRepository authorizationServerTokenRepository;
    @Autowired
    private OAuth2PermissionRepository oAuth2PermissionRepository;

    @Override
    public OAuth2AccessToken createAccessToken(OAuth2AuthorizationCode authorizationCode) {
        // 每次申请token都会使上一次申请的token失效（直接删掉吧）
        authorizationServerTokenRepository.deleteByClientIdAndUserId(authorizationCode.getClientId(), authorizationCode.getUserId());
        //生成新的token
        OAuth2AccessToken accessToken = new OAuth2AccessToken();
        accessToken.setAccessToken(UUID.randomUUID().toString().replaceAll("-", ""));
        accessToken.setRefreshToken(UUID.randomUUID().toString().replaceAll("-", ""));
        Date date = new Date();
        // refresh token的创建时间
        accessToken.setRefreshTokenCreateTime(date);
        // access token的创建时间
        accessToken.setAccessTokenCreateTime(date);
        accessToken.setExpireIn(7200L);
        accessToken.setScope(authorizationCode.getScopeAsSet());
        accessToken.setUserId(authorizationCode.getUserId());
        accessToken.setClientId(authorizationCode.getClientId());
        authorizationServerTokenRepository.save(accessToken);
        return accessToken;
    }

    @Override
    public OAuth2AccessToken refreshAccessToken(String refreshToken) {
        OAuth2AccessToken accessToken = authorizationServerTokenRepository.findByRefreshToken(refreshToken);
        if (accessToken == null) {
            throw new InvalidGrantException("Invalid refresh token");
        }
        if (accessToken.isRefreshTokenExpired()) {
            authorizationServerTokenRepository.deleteByRefreshToken(refreshToken);
            throw new InvalidGrantException("refresh token expired");
        }
        // 旧的refresh token继续使用，这里只做access token的生成
        String newAccessToken = UUID.randomUUID().toString().replaceAll("-", "");
        accessToken.setAccessToken(newAccessToken);
        accessToken.setAccessTokenCreateTime(new Date());
        // 更新access token
        return authorizationServerTokenRepository.save(accessToken);
    }

    @Override
    public Oauth2Authentication parseAccessToken(String accessToken) {
        OAuth2AccessToken oAuth2AccessToken = authorizationServerTokenRepository.findById(accessToken).orElseThrow(
                () -> new InvalidGrantException("Invalid access token")
        );
        if (oAuth2AccessToken.isAccessTokenExpired()) {
            throw new InvalidGrantException("access token expired");
        }
        String userId = oAuth2AccessToken.getUserId();
        String clientId = oAuth2AccessToken.getClientId();
        List<OAuth2Permission> permissions = oAuth2PermissionRepository.findAllById(oAuth2AccessToken.getScopeAsSet());
        return new Oauth2Authentication(userId, clientId, permissions);
    }
}
