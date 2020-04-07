package com.ht.ueditor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.ueditor.UeditorProperties;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UEditorService {
    private ObjectMapper objectMapper;
    private String rootPath;
    private String contextPath;
    private Map<String, Object> config;
    private UeditorProperties ueditorProperties;

    public UEditorService(UeditorProperties ueditorProperties, ObjectMapper objectMapper) {
        this.ueditorProperties = ueditorProperties;
        this.objectMapper = objectMapper;
    }

    public Map<String, Object> getUploadImageConfig(HttpServletRequest request) {
        Map<String, Object> globalConfig = getConfig(request);
        Map<String, Object> conf = new HashMap<>();
        String savePath;
        conf.put("isBase64", "false");
        conf.put("maxSize", globalConfig.get("imageMaxSize"));
        conf.put("allowFiles", globalConfig.get("imageAllowFiles"));
        conf.put("fieldName", globalConfig.get("imageFieldName"));
        savePath = ueditorProperties.getUploadDir() + globalConfig.get("imagePathFormat") + LocalDate.now().toString() + "/";

        conf.put("savePath", savePath);
        conf.put("contextPath", contextPath);
        conf.put("serverUrl", ueditorProperties.getServerUrl());
        conf.put("rootPath", rootPath);
        return conf;
    }

    public Map<String, Object> getUploadScrawlConfig(HttpServletRequest request) {
        Map<String, Object> globalConfig = getConfig(request);
        Map<String, Object> conf = new HashMap<>();
        String savePath;
        conf.put("filename", "scrawl");
        conf.put("maxSize", globalConfig.get("scrawlMaxSize"));
        conf.put("fieldName", globalConfig.get("scrawlFieldName"));
        conf.put("isBase64", "true");
        savePath = ueditorProperties.getUploadDir() + globalConfig.get("scrawlPathFormat") + LocalDate.now().toString() + "/";
        conf.put("savePath", savePath);
        conf.put("contextPath", contextPath);
        conf.put("serverUrl", ueditorProperties.getServerUrl());
        conf.put("rootPath", rootPath);
        return conf;
    }

    public Map<String, Object> getUploadVideoConfig(HttpServletRequest request) {
        Map<String, Object> globalConfig = getConfig(request);
        Map<String, Object> conf = new HashMap<>();
        String savePath;
        conf.put("maxSize", globalConfig.get("videoMaxSize"));
        conf.put("allowFiles", globalConfig.get("videoAllowFiles"));
        conf.put("fieldName", globalConfig.get("videoFieldName"));
        savePath = ueditorProperties.getUploadDir() + globalConfig.get("videoPathFormat") + LocalDate.now().toString() + "/";
        conf.put("savePath", savePath);
        conf.put("contextPath", contextPath);
        conf.put("serverUrl", ueditorProperties.getServerUrl());
        conf.put("rootPath", rootPath);
        return conf;
    }

    public Map<String, Object> getUploadFileConfig(HttpServletRequest request) {
        Map<String, Object> globalConfig = getConfig(request);
        Map<String, Object> conf = new HashMap<>();
        String savePath;
        conf.put("isBase64", "false");
        conf.put("maxSize", globalConfig.get("fileMaxSize"));
        conf.put("allowFiles", globalConfig.get("fileAllowFiles"));
        conf.put("fieldName", globalConfig.get("fileFieldName"));
        savePath = ueditorProperties.getUploadDir() + globalConfig.get("filePathFormat") + LocalDate.now().toString() + "/";

        conf.put("savePath", savePath);
        conf.put("contextPath", contextPath);
        conf.put("serverUrl", ueditorProperties.getServerUrl());
        conf.put("rootPath", rootPath);
        return conf;
    }

    public Map<String, Object> getCatchImageConfig(HttpServletRequest request) {
        Map<String, Object> globalConfig = getConfig(request);
        Map<String, Object> conf = new HashMap<>();
        String savePath;
        conf.put("filename", "remote");
        conf.put("filter", globalConfig.get("catcherLocalDomain"));
        conf.put("maxSize", globalConfig.get("catcherMaxSize"));
        conf.put("allowFiles", globalConfig.get("catcherAllowFiles"));
        conf.put("fieldName", globalConfig.get("catcherFieldName") + "[]");
        savePath = ueditorProperties.getUploadDir() + globalConfig.get("catcherPathFormat") + LocalDate.now().toString() + "/";
        conf.put("savePath", savePath);
        conf.put("contextPath", contextPath);
        conf.put("serverUrl", ueditorProperties.getServerUrl());
        conf.put("rootPath", rootPath);
        return conf;
    }

    public Map<String, Object> getListFileConfig(HttpServletRequest request) {
        Map<String, Object> globalConfig = getConfig(request);
        Map<String, Object> conf = new HashMap<>();
        conf.put("allowFiles", globalConfig.get("fileManagerAllowFiles"));
        conf.put("dir", ueditorProperties.getUploadDir() + globalConfig.get("fileManagerListPath"));
        conf.put("count", globalConfig.get("fileManagerListSize"));

        conf.put("contextPath", contextPath);
        conf.put("serverUrl", ueditorProperties.getServerUrl());
        conf.put("rootPath", rootPath);
        return conf;
    }

    public Map<String, Object> getListImageConfig(HttpServletRequest request) {
        Map<String, Object> globalConfig = getConfig(request);
        Map<String, Object> conf = new HashMap<>();
        conf.put("allowFiles", globalConfig.get("imageManagerAllowFiles"));
        conf.put("dir", ueditorProperties.getUploadDir() + globalConfig.get("imageManagerListPath"));
        conf.put("count", globalConfig.get("imageManagerListSize"));
        conf.put("contextPath", contextPath);
        conf.put("serverUrl", ueditorProperties.getServerUrl());
        conf.put("rootPath", rootPath);
        return conf;
    }

    public Map<String, Object> getConfig(HttpServletRequest request) {
        if (this.config != null && this.config.size() > 0) {
            return this.config;
        }
        synchronized (this) {
            if (this.config != null && this.config.size() > 0) {
                return this.config;
            }
            // 初始化配置
            // 文件上传目录ueditor,和config.json同一级目录
            try (InputStream is = UEditorService.class.getResourceAsStream(ueditorProperties.getConfigPath())) {
                this.config = objectMapper.readValue(is, Map.class);
            } catch (Exception e) {
                log.error("UEditor初始化失败", e);
            }
            this.rootPath = request.getServletContext().getRealPath("/");
            this.contextPath = request.getContextPath();
        }
        return this.config;
    }

    public String toJson(Map<String, Object> result) throws JsonProcessingException {
        return objectMapper.writeValueAsString(result);
    }
}
