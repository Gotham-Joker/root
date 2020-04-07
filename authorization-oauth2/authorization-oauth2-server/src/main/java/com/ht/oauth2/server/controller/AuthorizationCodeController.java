package com.ht.oauth2.server.controller;

import com.ht.authorization.Authentication;
import com.ht.authorization.AuthenticationContext;
import com.ht.authorization.exception.AuthenticationException;
import com.ht.commons.message.Result;
import com.ht.oauth2.OAuth2AccessTokenResponse;
import com.ht.oauth2.OAuth2AuthenticationRequest;
import com.ht.oauth2.exceptions.InvalidGrantException;
import com.ht.oauth2.exceptions.OAuth2Exception;
import com.ht.oauth2.model.OAuth2AccessToken;
import com.ht.oauth2.model.OAuth2AuthorizationCode;
import com.ht.oauth2.model.OAuth2Client;
import com.ht.oauth2.model.OAuth2Permission;
import com.ht.oauth2.server.Oauth2PermissionVO;
import com.ht.oauth2.server.Oauth2RequestInfo;
import com.ht.oauth2.service.client.OAuth2ClientService;
import com.ht.oauth2.service.code.OAuth2AuthorizationCodeService;
import com.ht.oauth2.service.permission.OAuth2PermissionService;
import com.ht.oauth2.service.token.AuthorizationServerTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Api(tags = "oauth2.0授权码模式")
public class AuthorizationCodeController {
    @Autowired
    private OAuth2AuthorizationCodeService authorizationCodeService;
    @Autowired
    private OAuth2ClientService oauth2ClientService;
    @Autowired
    private AuthorizationServerTokenService authorizationServerTokenService;
    @Autowired
    private OAuth2PermissionService oAuth2PermissionService;

    @RequestMapping(path = "/oauth/authorize", params = {"response_type=code"})
    @ApiOperation("请求授权码")
    @ApiImplicitParams({@ApiImplicitParam("client_id"), @ApiImplicitParam("redirect_uri"),
            @ApiImplicitParam("scope"), @ApiImplicitParam("response_type"), @ApiImplicitParam("state")})
    public ModelAndView authorize(
            @RequestParam("client_id") String clientId, @RequestParam String state,
            @RequestParam("redirect_uri") String redirectUri, @RequestParam("scope") String scopeValue, HttpSession session) {
        Set<String> scopes = null;
        if (StringUtils.hasText(scopeValue)) {
            scopes = Arrays.stream(scopeValue.split("\\+")).collect(Collectors.toSet());
        }
        // 封装请求
        OAuth2AuthenticationRequest request = new OAuth2AuthenticationRequest(
                clientId, redirectUri, "code", scopes, state);

        // 授权类型校验,client校验
        OAuth2Client client = oauth2ClientService.findById(request.getClientId());
        if (!client.getGrantTypeAsSet().contains("authorization_code")) {
            throw new InvalidGrantException("this client doesn't support the grant type: authorization_code");
        }
        // scope校验
        client.getScopeAsSet().forEach(scope -> {
            if (!request.getScope().contains(scope)) {
                throw new OAuth2Exception(500, "unsupported scope: " + scope);
            }
        });
        ModelAndView mav = new ModelAndView("redirect:/oauth/confirm.html");
        session.setAttribute("authorizationRequest", request);
        return mav;
    }

    @GetMapping("/oauth/request-info")
    @ResponseBody
    public Result<Oauth2RequestInfo> requestInfo(HttpSession session) {
        OAuth2AuthenticationRequest request = (OAuth2AuthenticationRequest) session.getAttribute("authorizationRequest");
        if (request == null) {
            return Result.error(500, "非法请求");
        }
        List<OAuth2Permission> list = oAuth2PermissionService.findByScopeIn(request.getScope());
        List<Oauth2PermissionVO> scopes = new ArrayList<>();
        Oauth2RequestInfo info = new Oauth2RequestInfo();
        info.setClientName(oauth2ClientService.findById(request.getClientId()).getClientName());
        if (list != null) {
            scopes.addAll(list.stream().map(Oauth2PermissionVO::new).collect(Collectors.toList()));
        }
        info.setScopes(scopes);
        return Result.success(info);
    }

    @RequestMapping(path = "/oauth-approve", method = RequestMethod.POST)
    @ApiOperation("用户授权，需要先登录")
    public ModelAndView approveOrDeny(@RequestParam("approve_scope") Set<String> userApprovalScopes,
                                      HttpSession session) {

        OAuth2AuthenticationRequest request = (OAuth2AuthenticationRequest) session.getAttribute("authorizationRequest");
        Authentication authentication = AuthenticationContext.current();
        try {
            if (authentication == null) {
                throw new AuthenticationException("User must be authenticated before authorizing an access token.");
            }
            if (request == null) {
                throw new OAuth2Exception(500, "Cannot approve uninitialized authorization request.");
            }
            if (userApprovalScopes == null || userApprovalScopes.isEmpty()) {
                throw new OAuth2Exception(403, "user denied access");
            }
            Set<String> requestScopes = request.getScope();
            Set<String> approvedScopes = userApprovalScopes.stream().filter(
                    requestScopes::contains).collect(Collectors.toSet());
            request.setScope(approvedScopes);
            String code = authorizationCodeService.createAuthorizationCode(authentication, request);
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append("redirect:").append(request.getRedirectUri()).append("?code=").append(code);
            if (StringUtils.hasText(request.getState())) {
                urlBuilder.append("&state=").append(request.getState());
            }
            return new ModelAndView(urlBuilder.toString());
        } finally {
            // 清除session数据
            session.invalidate();
        }
    }

    @RequestMapping(value = "/oauth/token", method = RequestMethod.POST, params = {"grant_type=authorization_code"})
    @ApiOperation("申请access_token")
    public ResponseEntity<OAuth2AccessTokenResponse> applyForAccessToken(@RequestParam Map<String, String> parameters) {
        String clientId = parameters.get("client_id");
        String clientSecret = parameters.get("client_secret");
        String code = parameters.get("code");
        if (!StringUtils.hasText(code)) {
            throw new OAuth2Exception(422, "code can not be empty");
        }
        OAuth2Client client = oauth2ClientService.findById(clientId);
        if (!client.getGrantTypeAsSet().contains("authorization_code")) {
            throw new OAuth2Exception(500, "this client does not support the grant type");
        }
        if (!client.getClientSecret().equals(clientSecret)) {
            throw new OAuth2Exception(401, "bad client secret");
        }
        OAuth2AuthorizationCode authorizationCode = authorizationCodeService.consumeAuthorizationCode(code);
        if (!authorizationCode.getClientId().equals(clientId)) {
            throw new OAuth2Exception(500, "clientId mismatch current clientId");
        }
        OAuth2AccessToken accessToken = authorizationServerTokenService.createAccessToken(authorizationCode);
        return getAccessTokenResponse(accessToken);
    }

    @RequestMapping(value = "/oauth/token", method = RequestMethod.POST, params = {"grant_type=refresh_token"})
    @ResponseBody
    @ApiOperation("refresh_token")
    public ResponseEntity<OAuth2AccessTokenResponse> refreshAccessToken(@RequestParam Map<String, String> parameters) {
        String refreshToken = parameters.get("refresh_token");
        String clientId = parameters.get("client_id");
        String clientSecret = parameters.get("client_secret");
        // 校验客户端id和密码
        OAuth2Client client = oauth2ClientService.findById(clientId);
        if (!client.getGrantTypeAsSet().contains("refresh_token")) {
            throw new OAuth2Exception(500, "this client does not support the grant type");
        }
        if (client.getClientSecret().equals(clientSecret)) {
            throw new OAuth2Exception(401, "bad client secret");
        }
        OAuth2AccessToken accessToken = authorizationServerTokenService.refreshAccessToken(refreshToken);
        if (!accessToken.getClientId().equals(clientId)) {
            throw new OAuth2Exception(500, "invalid client id for this refresh token");
        }
        return getAccessTokenResponse(accessToken);
    }

    private ResponseEntity<OAuth2AccessTokenResponse> getAccessTokenResponse(OAuth2AccessToken accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        headers.set("Content-Type", "application/json;charset=UTF-8");
        OAuth2AccessTokenResponse accessTokenResponse = new OAuth2AccessTokenResponse();
        BeanUtils.copyProperties(accessToken, accessTokenResponse);
        return new ResponseEntity<>(accessTokenResponse, headers, HttpStatus.OK);
    }

}
