package com.ht.ueditor.controller;

import com.ht.ueditor.service.UEditorService;
import com.ht.ueditor.utils.Downloader;
import com.ht.ueditor.utils.FileManager;
import com.ht.ueditor.utils.Uploader;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@ResponseBody
@RequestMapping("${com.ht.ueditor.api:api/ueditor}")
public class UEditorController {
    @Autowired
    UEditorService ueditorService;

    protected String getResponse(String callback, Map<String, Object> result) throws JsonProcessingException {
        if (callback != null) { // 如果是跨域，ueditor会发jsonp过来
            if (callback.matches("^[a-zA-Z_]+[\\w0-9_]*$")) {
                return callback + "(" + ueditorService.toJson(result) + ")";
            }
            throw new RuntimeException("jsonp异常");
        }
        return ueditorService.toJson(result);
    }

    /**
     * 获取ueditor的config.json配置
     *
     * @return
     * @throws IOException
     */
    @GetMapping(params = {"action=config"})
    public String config(HttpServletRequest request, @RequestParam(required = false) String callback) throws IOException {
        return getResponse(callback, ueditorService.getConfig(request));
    }

    /**
     * 上传图片
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(params = {"action=uploadimage"})
    public String uploadimage(HttpServletRequest request,
                              @RequestParam(required = false) String callback,
                              @RequestParam("upfile") MultipartFile file) throws IOException {
        Map<String, Object> conf = ueditorService.getUploadImageConfig(request);
        Map<String, Object> result = Uploader.upload(conf, file);
        result.put("state", "SUCCESS");
        return getResponse(callback, result);
    }

    @RequestMapping(params = {"action=uploadscrawl"})
    public String uploadscrawl(HttpServletRequest request,
                               @RequestParam(required = false) String callback,
                               @RequestParam("upfile") MultipartFile file) throws IOException {
        Map<String, Object> conf = ueditorService.getUploadScrawlConfig(request);
        Map<String, Object> result = Uploader.upload(conf, file);
        result.put("state", "SUCCESS");
        return getResponse(callback, result);
    }

    @RequestMapping(params = {"action=uploadvideo"})
    public String uploadvideo(HttpServletRequest request,
                              @RequestParam(required = false) String callback,
                              @RequestParam("upfile") MultipartFile file) throws IOException {
        Map<String, Object> conf = ueditorService.getUploadVideoConfig(request);
        Map<String, Object> result = Uploader.upload(conf, file);
        result.put("state", "SUCCESS");
        return getResponse(callback, result);
    }

    @RequestMapping(params = {"action=uploadfile"})
    public String uploadfile(HttpServletRequest request,
                             @RequestParam(required = false) String callback,
                             @RequestParam("upfile") MultipartFile file) throws IOException {
        Map<String, Object> conf = ueditorService.getUploadFileConfig(request);
        Map<String, Object> result = Uploader.upload(conf, file);
        result.put("state", "SUCCESS");
        return getResponse(callback, result);
    }

    @RequestMapping(params = {"action=catchimage"})
    public String catchimage(HttpServletRequest request, @RequestParam(required = false) String callback) throws Exception {
        Map<String, Object> conf = ueditorService.getCatchImageConfig(request);
        // ueditor传入远程文件url地址，咱们把他下载过来，并按照官方文档要求的格式返回
        String[] list = request.getParameterValues((String) conf.get("fieldName"));
        Map<String, Object> result = Downloader.download(conf, list);
        return getResponse(callback, result);
    }

    @RequestMapping(params = {"action=listfile"})
    public String listfile(HttpServletRequest request,
                           @RequestParam(required = false) String callback,
                           @RequestParam Integer start) throws IOException {
        Map<String, Object> conf = ueditorService.getListFileConfig(request);
        Map<String, Object> result = new FileManager(conf).listFile(start);
        return getResponse(callback, result);
    }


    @RequestMapping(params = {"action=listimage"})
    public String listimage(HttpServletRequest request,
                            @RequestParam(required = false) String callback,
                            @RequestParam Integer start) throws IOException {
        Map<String, Object> conf = ueditorService.getListImageConfig(request);
        Map<String, Object> result = new FileManager(conf).listFile(start);
        return getResponse(callback, result);
    }


}
