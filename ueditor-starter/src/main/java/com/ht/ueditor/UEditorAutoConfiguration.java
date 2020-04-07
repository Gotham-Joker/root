package com.ht.ueditor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.oss.autoconfigure.OssService;
import com.ht.ueditor.controller.OssUEditorController;
import com.ht.ueditor.controller.UEditorController;
import com.ht.ueditor.service.UEditorService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UEditorAutoConfiguration {

    @Bean
    public UeditorProperties ueProperties() {
        return new UeditorProperties();
    }

    @Bean
    @ConditionalOnMissingBean(name = "ueditorService")
    public UEditorService ueditorService(UeditorProperties ueditorProperties) {
        return new UEditorService(ueditorProperties, new ObjectMapper());
    }

    @Bean
    @ConditionalOnProperty(prefix = "com.ht.ueditor", name = "enable-oss", havingValue = "true", matchIfMissing = true)
    @ConditionalOnBean(OssService.class)
    public OssUEditorController ossUEditorController() {
        return new OssUEditorController();
    }

    @Bean
    @ConditionalOnMissingBean(value = UEditorController.class)
    public UEditorController ueditorController() {
        return new UEditorController();
    }

}
