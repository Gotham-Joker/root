package com.ht.authorization.filter;

import com.ht.authorization.Permission;
import com.ht.authorization.PermissionService;
import com.ht.authorization.event.PermissionChangedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;

import java.util.List;

@Slf4j
public class DefaultSecurityFilter extends AbstractSecurityFilter implements ApplicationListener<PermissionChangedEvent> {

    public DefaultSecurityFilter(AuthenticationManager authenticationManager, PermissionService permissionService, List<String> permitUrls) {
        super(authenticationManager, permissionService, permitUrls);
    }

    @Override
    public void onApplicationEvent(PermissionChangedEvent event) {
        Permission changedPermission = (Permission) event.getSource();
        switch (event.getEventType()) {
            case ADD:
                super.addPermission(changedPermission);
                break;
            case UPDATE:
                super.updatePermission(changedPermission);
                break;
            case DELETE:
                super.removePermission(changedPermission);
                break;
            default:
                break;
        }
    }


}
