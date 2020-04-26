package com.ht.weixin.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.RedpackService;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.RedpackServiceImpl;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(WeiXinProperties.class)
public class WeiXinAutoConfiguration {
    private final WeiXinProperties weiXinProperties;

    /**
     * 微信公众号服务类
     * @return
     */
    @Bean
    public WxMpService wxMpService() {
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(weiXinProperties.getAppId()); // 设置微信公众号的appId
        config.setSecret(weiXinProperties.getAppSecret()); // 设置微信公众号的appSecret
        WxMpService wxService = new WxMpServiceImpl();
        wxService.setWxMpConfigStorage(config);
        return wxService;
    }

    /**
     * 如果配置了商户id，则自动配置微信支付服务类
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "com.ht.wx", value = "mch-id")
    public WxPayService wxPayService() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(weiXinProperties.getAppId());
        payConfig.setMchId(weiXinProperties.getMchId());
        payConfig.setMchKey(weiXinProperties.getMchKey());
        payConfig.setKeyPath(weiXinProperties.getKeyPath());
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    /**
     * 微信红包服务类
     *
     * @return
     */
    @Bean
    @ConditionalOnBean(WxPayService.class)
    public RedpackService redpackService() {
        return new RedpackServiceImpl(wxPayService());
    }

}
