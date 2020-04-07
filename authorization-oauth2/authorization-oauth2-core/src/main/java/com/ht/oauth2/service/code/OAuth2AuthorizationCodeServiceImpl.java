package com.ht.oauth2.service.code;

import com.ht.authorization.Authentication;
import com.ht.oauth2.exceptions.OAuth2Exception;
import com.ht.oauth2.repository.OAuth2AuthorizationCodeRepository;
import com.ht.oauth2.OAuth2AuthenticationRequest;
import com.ht.oauth2.model.OAuth2AuthorizationCode;
import com.ht.oauth2.util.RandomStringGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class OAuth2AuthorizationCodeServiceImpl implements OAuth2AuthorizationCodeService {
    @Autowired
    private OAuth2AuthorizationCodeRepository authorizationCodeRepository;
    private RandomStringGenerator generator = new RandomStringGenerator();

    @Override
    public String createAuthorizationCode(Authentication authentication, OAuth2AuthenticationRequest request) {
        // 每次生成code都会使上一个code失效
        authorizationCodeRepository.deleteByUserIdAndClientId(authentication.getId(), request.getClientId());
        // 把生成的code和用户的授权信息保存到数据库中
        String code = UUID.randomUUID().toString().replaceAll("-", "") + generator.generate();
        OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode();
        BeanUtils.copyProperties(request, authorizationCode);
        authorizationCode.setUserId(authentication.getId());
        authorizationCode.setCode(code);
        authorizationCode.setCreateTime(new Date());
        authorizationCodeRepository.save(authorizationCode);
        return code;
    }

    @Override
    public OAuth2AuthorizationCode consumeAuthorizationCode(String code) {
        OAuth2AuthorizationCode authorizationCode = authorizationCodeRepository.findById(code).orElseThrow(
                () -> new OAuth2Exception(500,"invalid code"));
        authorizationCodeRepository.delete(authorizationCode);
        //code 5分钟后失效
        if (System.currentTimeMillis() - authorizationCode.getCreateTime().getTime() > 300 * 1000) {
            throw new OAuth2Exception(500,"code expired");
        }
        return authorizationCode;
    }

}
