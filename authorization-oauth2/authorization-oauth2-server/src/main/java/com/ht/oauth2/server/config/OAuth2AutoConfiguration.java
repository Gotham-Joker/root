package com.ht.oauth2.server.config;

import com.ht.oauth2.server.filter.OAuth2SecurityFilter;
import com.ht.oauth2.server.filter.Oauth2AuthenticationManager;
import com.ht.oauth2.service.permission.OAuth2PermissionService;
import com.ht.oauth2.service.token.AuthorizationServerTokenService;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("com.ht.oauth2.model")
@EnableJpaRepositories("com.ht.oauth2.repository")
@ComponentScan("com.ht.oauth2")
@Configuration
public class OAuth2AutoConfiguration {


    @Bean
    public OAuth2SecurityFilter oAuth2AuthenticationFilter(AuthorizationServerTokenService authorizationServerTokenService, OAuth2PermissionService oAuth2PermissionService) {
        Oauth2AuthenticationManager manager = new Oauth2AuthenticationManager(authorizationServerTokenService);
        return new OAuth2SecurityFilter(manager, oAuth2PermissionService, null);
    }

    /**
     * OAuth2.0过滤器，只负责/oauth-api/* 接口的处理
     *
     * @param oAuth2AuthenticationFilter
     * @return
     */
    @Bean
    public FilterRegistrationBean<OAuth2SecurityFilter> oAuth2SecurityFilter(OAuth2SecurityFilter oAuth2AuthenticationFilter) {
        FilterRegistrationBean<OAuth2SecurityFilter> bean = new FilterRegistrationBean<>(oAuth2AuthenticationFilter);
        bean.setOrder(3);
        bean.addUrlPatterns("/oauth-api/*");
        return bean;
    }
}
