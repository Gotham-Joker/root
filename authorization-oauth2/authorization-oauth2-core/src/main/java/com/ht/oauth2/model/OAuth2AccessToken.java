package com.ht.oauth2.model;

import lombok.Data;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Entity
@Table(name = "oauth2_access_token", uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "clientId", "refreshToken"})})
public class OAuth2AccessToken {
    @Id
    private String accessToken;
    private String userId;
    private String clientId;
    private Long expireIn;
    private String refreshToken;
    private Date refreshTokenCreateTime;
    private Date accessTokenCreateTime;
    private String scope;

    @Transient
    public Set<String> getScopeAsSet() {
        if (StringUtils.hasText(scope)) {
            return Stream.of(scope.split(",")).collect(Collectors.toSet());
        }
        return Collections.EMPTY_SET;
    }

    public void setScope(Set<String> scope) {
        if (scope == null || scope.isEmpty()) {
            this.scope = null;
        } else {
            this.scope = String.join(",", scope);
        }
    }

    @Transient
    public boolean isRefreshTokenExpired() {
        if (System.currentTimeMillis() - refreshTokenCreateTime.getTime() > 7 * 24 * 3600 * 1000) {
            return true;
        }
        return false;
    }

    @Transient
    public boolean isAccessTokenExpired() {
        if (System.currentTimeMillis() - accessTokenCreateTime.getTime() > expireIn * 1000) {
            return true;
        }
        return false;
    }
}
