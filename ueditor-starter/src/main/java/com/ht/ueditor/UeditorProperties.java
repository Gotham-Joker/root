package com.ht.ueditor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

@Slf4j
@Data
@ConfigurationProperties(prefix = "com.ht.ueditor")
public class UeditorProperties {
    /**
     * config.json文件位置
     * 默认"/config.json"
     */
    private String configPath = "/config.json";
    /**
     * 文件存放目录，必须"/"开头
     * 默认"/ueditor"
     */
    private String uploadDir = "/ueditor";

    /**
     * 上传文件后将会返回绝对路径
     */
    private String serverUrl;

    /**
     * UEditorController的mapping，默认"/api/ueditor"
     */
    private String api = "/api/ueditor";

    /**
     * 是否启用oss上传，默认true
     */
    private Boolean enableOss;


    @PostConstruct
    public void init() {
        log.info("ueditor的api地址: {}",api);
    }
}
