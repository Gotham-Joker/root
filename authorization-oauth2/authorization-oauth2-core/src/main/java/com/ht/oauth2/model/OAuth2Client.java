package com.ht.oauth2.model;

import lombok.Data;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Entity
@Table(name = "oauth2_client")
public class OAuth2Client {
    @Id
    private String clientId;
    private String clientSecret;
    private String clientName;
    private String scope;
    /**
     * 1 启用，0 禁用
     */
    private Byte status;
    private String redirectUri;
    //支持的授权类型
    private String grantTypes;
    private String ownerId;

    @Transient
    public Set<String> getScopeAsSet() {
        if (StringUtils.hasText(scope)) {
            return Stream.of(scope.split(",")).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    public void setScope(Set<String> grantTypes) {
        if (grantTypes == null || grantTypes.isEmpty()) {
            this.scope = null;
        } else {
            this.scope = String.join(",", grantTypes);
        }
    }

    @Transient
    public Set<String> getGrantTypeAsSet() {
        if (StringUtils.hasText(grantTypes)) {
            return Stream.of(grantTypes.split(",")).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    public void setGrantTypes(Set<String> grantTypes) {
        if (grantTypes == null || grantTypes.isEmpty()) {
            this.grantTypes = null;
        } else {
            this.grantTypes = String.join(",", grantTypes);
        }
    }

}
