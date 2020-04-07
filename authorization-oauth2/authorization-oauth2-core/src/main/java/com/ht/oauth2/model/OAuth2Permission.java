package com.ht.oauth2.model;

import com.ht.authorization.Permission;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "oauth2_permission")
public class OAuth2Permission implements Permission {
    @Id
    private String id;
    private String name;
    private String method;
    private String url;
    private String scope;

}
