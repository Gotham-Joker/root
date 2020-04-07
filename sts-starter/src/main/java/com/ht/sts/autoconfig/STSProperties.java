package com.ht.sts.autoconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "com.ht.sts")
public class STSProperties {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String roleArn;
    private String bucketName;
    private String region;
//    private String policy;
    /**
     * sts token有效期，单位s
     */
    private Long tokenTtl;
}
