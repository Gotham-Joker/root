package com.ht.oss.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@Data
@ConfigurationProperties(prefix = "com.ht.oss")
public class OssProperties {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    /**
     * bucket的公网访问地址
     */
    private String bucketDomain;
    /**
     * 允许上传的文件扩展名 eg: .jpg,.mp4,.zip
     */
    private Set<String> suffixes;
}
