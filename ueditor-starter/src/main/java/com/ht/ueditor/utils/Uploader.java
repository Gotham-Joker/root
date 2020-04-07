package com.ht.ueditor.utils;

import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Uploader {
    public static Map<String, Object> upload(Map<String, Object> conf, MultipartFile file) throws IOException {
        Map<String, Object> map = new HashMap<>();
        String savePath = (String) conf.get("savePath");
        String suffix;
        int index;
        List<String> suffixes = (List<String>) conf.get("allowFiles");
        long maxSize = Long.parseLong(conf.get("maxSize") + "");
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new RuntimeException("文件名获取失败");
        }
        index = fileName.lastIndexOf(".");
        if (index == -1) {
            throw new RuntimeException("文件格式错误");
        }
        suffix = fileName.substring(index).toLowerCase();
        if (!suffixes.contains(suffix)) {
            throw new RuntimeException("不支持的文件格式");
        }
        long size = file.getBytes().length;
        if (size > maxSize) {
            throw new RuntimeException("超出文件上传大小限制");
        }
        String guid = UUID.randomUUID().toString().replace("-", "");
        String filePath = conf.get("rootPath") + savePath + "/";
        String contextPath = (String) conf.get("contextPath");
        String serverUrl = (String) conf.get("serverUrl");
        new File(filePath).mkdirs();
        try (InputStream is = file.getInputStream();
             FileOutputStream fos = new FileOutputStream(new File(filePath + guid + suffix))) {
            FileCopyUtils.copy(is, fos);
        }
        map.put("state", "SUCCESS");
        map.put("size", size + "");
        map.put("title", guid + suffix);
        map.put("type", suffix);
        map.put("original", fileName);
        map.put("url", contextPath + savePath + guid + suffix);
        if (StringUtils.hasText(serverUrl)) {
            map.put("url", serverUrl + contextPath + savePath + guid + suffix);
        }
        return map;
    }
}
