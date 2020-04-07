package com.ht.ueditor.controller;


import com.ht.oss.autoconfigure.OssService;
import com.ht.ueditor.UEditorException;
import com.ht.ueditor.UeditorProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class OssUEditorController extends UEditorController {
    @Autowired
    private OssService ossService;
    @Autowired
    private UeditorProperties ueditorProperties;
    private String uploadDir;

    @PostConstruct
    public void init() {
        this.uploadDir = ueditorProperties.getUploadDir().replace("/", "");
    }

    private String upload(String callback, MultipartFile file) throws IOException {
        // 获取扩展名和文件哈希值作为文件名
        String suffix = ossService.getSuffix(file.getOriginalFilename());
        if (!ossService.isSuffixValid(suffix)) {
            throw new UEditorException("不支持上传该文件");
        }
        String hash;
        try (InputStream is = file.getInputStream()) {
            hash = ossService.getSHA(is);
        }
        Map<String, Object> result = new HashMap<>();
        try (InputStream is = file.getInputStream()) {
            String fileName = hash + suffix;
            String url = ossService.upload(uploadDir + "/" + fileName, is);
            result.put("state", "SUCCESS");
            result.put("size", file.getSize());
            result.put("title", fileName);
            result.put("type", suffix);
            result.put("original", fileName);
            result.put("url", url);
        }
        return getResponse(callback, result);
    }

    @Override
    public String uploadimage(HttpServletRequest request,
                              @RequestParam(required = false) String callback,
                              @RequestParam("upfile") MultipartFile file) throws IOException {
        return upload(callback, file);
    }

    @Override
    public String uploadfile(HttpServletRequest request,
                             @RequestParam(required = false) String callback,
                             @RequestParam("upfile") MultipartFile file) throws IOException {
        return upload(callback, file);
    }

    @Override
    public String uploadscrawl(HttpServletRequest request,
                               @RequestParam(required = false) String callback,
                               @RequestParam("upfile") MultipartFile file) throws IOException {
        return upload(callback, file);
    }

    @Override
    public String uploadvideo(HttpServletRequest request,
                              @RequestParam(required = false) String callback,
                              @RequestParam("upfile") MultipartFile file) throws IOException {
        return upload(callback, file);
    }

}
