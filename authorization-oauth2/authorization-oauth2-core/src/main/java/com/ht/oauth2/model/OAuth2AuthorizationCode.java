package com.ht.oauth2.model;

import lombok.Data;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Entity
@Table(name = "oauth2_authorization_code")
public class OAuth2AuthorizationCode {

    @Id
    private String code;
    private String userId;
    private String clientId;
    private String redirectUri;
    private String scope;
    private String state;
    private String responseType;
    private Date createTime;

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

}
