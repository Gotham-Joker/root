package com.ht.oauth2.repository;

import com.ht.oauth2.model.OAuth2Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface OAuth2PermissionRepository extends JpaRepository<OAuth2Permission,String> {
    List<OAuth2Permission> findByScopeIn(Set<String> scopes);
}
