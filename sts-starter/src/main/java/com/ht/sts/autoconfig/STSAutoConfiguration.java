package com.ht.sts.autoconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(STSProperties.class)
public class STSAutoConfiguration {

    @Bean
    public STSService stsService(@Autowired STSProperties stsProperties) {
        return new STSService(stsProperties);
    }
}
