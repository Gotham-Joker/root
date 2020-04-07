package com.ht.sts.autoconfig;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class STSService {
    private STSProperties stsProperties;

    public STSService(STSProperties stsProperties) {
        this.stsProperties = stsProperties;
    }

    public STSToken stsToken() throws ClientException {
        DefaultProfile.addEndpoint("", "", "Sts", stsProperties.getEndpoint());
        // 构造default profile（参数留空，无需添加region ID）
        IClientProfile profile = DefaultProfile.getProfile("", stsProperties.getAccessKeyId(), stsProperties.getAccessKeySecret());
        // 用profile构造client
        DefaultAcsClient client = new DefaultAcsClient(profile);
        final AssumeRoleRequest request = new AssumeRoleRequest();
        request.setMethod(MethodType.POST);
        request.setRoleArn(stsProperties.getRoleArn());
        request.setRoleSessionName("role-session-name");
//        request.setPolicy(policy); // 若policy为空，则用户将获得该角色下所有权限
        request.setDurationSeconds(stsProperties.getTokenTtl()); // 设置凭证有效时间
        final AssumeRoleResponse response = client.getAcsResponse(request);
        STSToken stsToken = new STSToken();
        stsToken.setAccessKeyId(response.getCredentials().getAccessKeyId());
        stsToken.setAccessKeySecret(response.getCredentials().getAccessKeySecret());
        stsToken.setStsToken(response.getCredentials().getSecurityToken());
        stsToken.setRegion(stsProperties.getRegion());
        stsToken.setBucket(stsProperties.getBucketName());
        return stsToken;
    }
}
