package com.ht.oss.autoconfigure;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Set;

public class OssService {
    private OSS oss;
    private OssProperties ossProperties;

    public OssService(OssProperties ossProperties) {
        this.oss = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
        this.ossProperties = ossProperties;
    }

    /**
     * 上传文件，返回文件的完整URL地址
     * 如果要访问缩略图，可以在图片地址后加上 ?x-oss-process=image/resize,m_lfit,h_100,w_100
     * OSS缩略图其他参数链接：https://help.aliyun.com/document_detail/44688.html?spm=a2c4g.11186623.4.2.7d384c0742hbzG
     * <p>
     * 如果是视频文件，获取第一帧：
     * ?x-oss-process=video/snapshot,t_10000,m_fast
     *
     * @param objectName
     * @return
     */
    public String upload(String objectName, InputStream is) {
        oss.putObject(ossProperties.getBucketName(), objectName, is);
        return ossProperties.getBucketDomain() + objectName;
    }

    public void deleteByObjectName(String objectName) {
        oss.deleteObject(ossProperties.getBucketName(), objectName);
    }

    public void deleteByUrl(String url) {
        String objectName = url.replace(ossProperties.getBucketDomain(), "");
        deleteByObjectName(objectName);
    }

    /**
     * 下载文件
     *
     * @param objectName 需要下载的文件
     * @param desFile    文件保存路径
     */
    public File download(String objectName, String desFile) {
        File file = new File(desFile);
        oss.getObject(new GetObjectRequest(ossProperties.getBucketName(), objectName), file);
        return file;
    }

    /**
     * 文件扩展名是否有效
     *
     * @param suffix
     * @return
     */
    public boolean isSuffixValid(String suffix) {
        Set<String> suffixes = ossProperties.getSuffixes();
        if (suffixes != null && !suffixes.isEmpty()) {
            return suffixes.contains(suffix.toLowerCase());
        }
        return true;
    }

    public String getSuffix(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return "";
        }
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        return fileName.substring(index);
    }

    /**
     * 获取文件流的 SHA-1 摘要值
     *
     * @param is
     * @return
     */
    public String getSHA(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            int len;
            byte[] buffer = new byte[4096];
            while ((len = is.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            byte[] bytes = md.digest();
            // 转16进制
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%x", bytes[i]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public OSS getOss() {
        return oss;
    }
}
