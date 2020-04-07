package com.ht.oauth2.repository;

import com.ht.oauth2.model.OAuth2Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Oauth2ClientRepository extends JpaRepository<OAuth2Client,String> {
}
