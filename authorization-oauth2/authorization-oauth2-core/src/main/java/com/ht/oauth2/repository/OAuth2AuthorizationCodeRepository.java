package com.ht.oauth2.repository;

import com.ht.oauth2.model.OAuth2AuthorizationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuth2AuthorizationCodeRepository extends JpaRepository<OAuth2AuthorizationCode,String> {
    void deleteByUserIdAndClientId(String id, String clientId);
}
