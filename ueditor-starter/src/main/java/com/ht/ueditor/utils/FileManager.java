package com.ht.ueditor.utils;


import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class FileManager {
    private String dir;
    private String rootPath;
    private Set<String> allowFiles;
    private String serverUrl;
    private String contextPath;
    private int count;

    public FileManager(Map<String, Object> conf) {
        this.rootPath = (String) conf.get("rootPath");
        this.contextPath = (String) conf.get("contextPath");
        this.serverUrl = (String) conf.get("serverUrl");
        this.dir = this.rootPath + conf.get("dir");
        this.allowFiles = new HashSet<>((List<String>) conf.get("allowFiles"));
        this.count = (Integer) conf.get("count");
    }


    public Map<String, Object> listFile(int start) {
        File dir = new File(this.dir);
        if (!dir.exists()) {
            throw new RuntimeException("ueditor文件目录不存在:" + this.dir);
        }
        if (!dir.isDirectory()) {
            throw new RuntimeException(this.dir + "不是目录");
        }
        // 递归查找所有后缀名的文件
        List<File> list = collectFile(dir);
        Map<String, Object> result = new HashMap<>();
        if (start >= 0 && start <= list.size()) {
            List<File> fileList = subFileList(list, start, start + count);
            List<Map<String, Object>> resultList = fileList.stream().map(file -> {
                Map<String, Object> map = new HashMap<>();
                map.put("state", "SUCCESS");
                String filePath = "/" + file.getPath().replace(this.rootPath, "").replaceAll("\\\\", "/");
                if (StringUtils.hasText(serverUrl)) {
                    map.put("url", serverUrl + contextPath + filePath);
                } else {
                    map.put("url", contextPath + filePath);
                }
                return map;
            }).collect(Collectors.toList());
            result.put("list", resultList);
        }
        result.put("state", "SUCCESS");
        result.put("start", start);
        result.put("total", list.size());
        return result;
    }

    private List<File> subFileList(List<File> files, int start, int end) {
        List<File> list = new ArrayList<>();
        for (int i = start; i < files.size() && i < end; i++) {
            list.add(files.get(i));
        }
        return list;
    }

    private List<File> collectFile(File dir) {
        File[] files = dir.listFiles();
        List<File> list = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (!file.isDirectory()) {
                String fileName = file.getName();
                int suffixIndex = fileName.lastIndexOf(".");
                if (suffixIndex == -1) {
                    return list;
                }
                String suffix = fileName.substring(suffixIndex);
                if (allowFiles.contains(suffix))
                    list.add(file);
            } else {
                list.addAll(collectFile(file));
            }
        }
        return list;
    }
}
