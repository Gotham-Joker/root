package com.ht.authorization.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.authorization.*;
import com.ht.authorization.exception.AuthenticationException;
import com.ht.authorization.exception.PermissionDeniedException;
import com.ht.commons.message.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractSecurityFilter extends GenericFilterBean {
    private List<AntPathRequestMatcher> permitRequestMatchers;
    /*url与权限的映射*/
    private static final Map<String, Set<Permission>> inMemoryPermissions = new ConcurrentHashMap<>();
    /*维护权限id与url的映射关系*/
    private static final Map<String, String> permissionUrlMapping = new ConcurrentHashMap<>();
    private Lock lock = new ReentrantLock();
    private AuthenticationManager authenticationManager;

    protected AbstractSecurityFilter(AuthenticationManager authenticationManager, PermissionService permissionService, List<String> permitUrls) {
        this.authenticationManager = authenticationManager;
        List<? extends Permission> list = permissionService.findAll();
        if (list != null && !list.isEmpty()) {
            list.forEach(this::addPermission);
        }

        if (permitUrls == null || permitUrls.isEmpty()) {
            permitRequestMatchers = Collections.emptyList();
        } else {
            permitRequestMatchers = permitUrls.stream().map(AntPathRequestMatcher::new).collect(Collectors.toList());
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = ((HttpServletRequest) request);
        HttpServletResponse resp = ((HttpServletResponse) response);
        log.debug("****  access uri:{},method:{}  ****", req.getRequestURI(), req.getMethod());
        // 允许通行的url
        if (permitRequestMatchers.stream().anyMatch(matcher -> matcher.matches(req))
                || req.getMethod().equalsIgnoreCase("options")) {
            chain.doFilter(request, response);
            return;
        }

        // 获取认证信息，鉴权
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(req, resp);
        } catch (AuthenticationException e) {
            log.error("{},ip:{},uri:{}", e.getMessage(), req.getLocalAddr(), req.getRequestURI(), e);
            sendError(resp, 401, e.getMessage());
            return;
        }

        List<? extends Permission> userPermissions = authentication.getPermissions();
        if (userPermissions == null || userPermissions.isEmpty()) {
            log.error("permission denied,authenticationId:{} have none permission", authentication.getId());
            sendError(resp, 403, "access denied");
            return;
        }

        Set<String> userPermissionIds = userPermissions.stream()
                .map(Permission::getId).collect(Collectors.toSet());

        try {
            AuthenticationContext.setAuthentication(authentication);

            // 判断用户访问的url是否是系统敏感的url
            for (String url : inMemoryPermissions.keySet()) {

                // 如果是，那就判断用户的权限是否足够
                if (new AntPathRequestMatcher(url).matches(req)) {
                    Set<Permission> needsPermissions = inMemoryPermissions.get(url);
                    for (Permission sp : needsPermissions) {
                        if (userPermissionIds.contains(sp.getId())) {
                            if (sp.getMethod() == null || sp.getMethod().equals("*") || sp.getMethod().equalsIgnoreCase(req.getMethod())) {
                                // 有访问权限，那就放行
                                chain.doFilter(request, response);
                                return;
                            }
                        }
                    }
                    // 没有访问权限
                    throw new PermissionDeniedException("access denied");
                }
            }

            // 当前访问的url不是敏感的url，放行
            chain.doFilter(request, response);
        } catch (PermissionDeniedException e) {
            log.error("{},authenticationId:{},uri:{}", e.getMessage(), authentication.getId(), req.getRequestURI());
            sendError(resp, e.getCode(), e.getMessage());
        } finally {
            AuthenticationContext.clearContext();
        }

    }

    /**
     * 添加指定的权限到缓存
     *
     * @param permission
     */
    protected void addPermission(Permission permission) {
        if (!StringUtils.hasText(permission.getUrl())) {
            return;
        }
        permissionUrlMapping.put(permission.getId(), permission.getUrl());

        try {
            lock.lock();
            Set<Permission> set = inMemoryPermissions.get(permission.getUrl());
            if (set == null) {
                set = new HashSet<>();
            }
            set.add(permission);
            inMemoryPermissions.put(permission.getUrl(), set);
        } finally {
            lock.unlock();
        }
    }

    protected void sendError(HttpServletResponse resp, int code, String msg) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(Result.error(code, msg));
        PrintWriter out = resp.getWriter();
        out.write(result);
        out.close();
    }

    /**
     * 从内存移出指定的权限
     */
    protected void removePermission(Permission permission) {
        String permissionId = permission.getId();
        String url = permissionUrlMapping.get(permissionId);
        permissionUrlMapping.remove(permissionId);
        try {
            lock.lock();
            Set<String> permissionIdSet = inMemoryPermissions.get(url).stream()
                    .map(Permission::getId).collect(Collectors.toSet());
            removeOldPermission(permissionId, url, permissionIdSet);
        } finally {
            lock.unlock();
        }
    }

    private void removeOldPermission(String permissionId, String oldUrl, Set<String> permissions) {
        if (permissions != null && !permissions.isEmpty()) {
            permissions.remove(permissionId);
            if (permissions.isEmpty()) {
                inMemoryPermissions.remove(oldUrl);
            }
        }
    }

    /**
     * 更新指定的权限到内存，先移除旧的再添加更新后的
     *
     * @param permission 更新后的权限
     */
    protected void updatePermission(Permission permission) {
        String permissionId = permission.getId();

        String oldUrl = permissionUrlMapping.get(permissionId);

        try {
            lock.lock();
            Set<String> permissionIdSet = inMemoryPermissions.get(oldUrl).stream()
                    .map(Permission::getId).collect(Collectors.toSet());
            removeOldPermission(permissionId, oldUrl, permissionIdSet);
            addPermission(permission);
        } finally {
            lock.unlock();
        }

    }

}
