package com.ht.sts.autoconfig;

import lombok.Data;

@Data
public class STSToken {
    private String region;
    private String accessKeyId;
    private String accessKeySecret;
    private String stsToken;
    private String bucket;
}
