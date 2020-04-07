package com.ht.ueditor.utils;

import org.springframework.http.HttpMethod;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.*;

public class Downloader {

    public static Map<String, Object> download(Map<String, Object> conf, String[] urls) throws Exception {
        Set<String> suffixes = new HashSet<>((List<String>) conf.get("allowFiles"));
        Set<String> filters = new HashSet<>((List<String>) conf.get("filter"));
        String savePath = (String) conf.get("savePath");
        String filePath = conf.get("rootPath") + savePath + "/";
        String contextPath = (String) conf.get("contextPath");
        String serverUrl = (String) conf.get("serverUrl");

        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> list = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < urls.length; i++) {
            String url = urls[i];
            int index = url.lastIndexOf(".");
            if (index == -1) {
                throw new RuntimeException("不支持下载该文件类型");
            }
            String suffix = url.substring(index);
            if (!suffixes.contains(suffix)) {
                throw new RuntimeException("不支持下载该文件类型");
            }
            String host = new URI(url).getHost();
            if (!filters.contains(host)) {
                throw new RuntimeException("不支持下载该域名下的文件");
            }
            String guid = UUID.randomUUID().toString().replaceAll("-", "");
            restTemplate.execute(url, HttpMethod.GET, null, response -> {
                File file = new File(filePath + guid + suffix);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    StreamUtils.copy(response.getBody(), fos); // 缓冲下载
                }
                return file;
            });
            Map<String, Object> map = new HashMap<>();
            map.put("state", "SUCCESS");
            map.put("source", url);
            map.put("url", contextPath + savePath + guid + suffix);
            if (StringUtils.hasText(serverUrl)) {
                map.put("url", serverUrl + contextPath + savePath + guid + suffix);
            }
            list.add(map);

        }
        result.put("state", "SUCCESS");
        result.put("list", list);
        return result;
    }

}
