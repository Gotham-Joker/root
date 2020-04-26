package com.ht.weixin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "com.ht.wx")
public class WeiXinProperties {
    /**
     * 设置微信公众号或者小程序等的appid
     */
    private String appId;

    /**
     * 设置微信公众号或者小程序等的密钥
     */
    private String appSecret;

    /**
     * 微信支付商户号
     */
    private String mchId;

    /**
     * 微信支付商户API密钥,32位
     */
    private String mchKey;

    /**
     * apiclient_cert.p12文件的绝对路径，或者如果放在项目中，请以classpath:开头指定
     */
    private String keyPath;
}