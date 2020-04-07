package com.ht.authorization.event;

import com.ht.authorization.Permission;
import com.ht.authorization.event.enums.PermissionChangeTypeEnum;
import com.ht.authorization.filter.DefaultSecurityFilter;
import org.springframework.context.ApplicationEvent;

/**
 * 权限更新事件通知，当权限被更新时，应当通知权限过滤器{@link DefaultSecurityFilter}更新权限
 * 可以publish一个event，权限过滤器收到这个事件会自动更新缓存
 */
public class PermissionChangedEvent extends ApplicationEvent {
    private PermissionChangeTypeEnum eventType;

    /**
     * Create a new ApplicationEvent.
     *
     * @param permission 修改后的权限对象
     */
    public PermissionChangedEvent(Permission permission, PermissionChangeTypeEnum eventType) {
        super(permission);
        this.eventType = eventType;
    }

    public PermissionChangeTypeEnum getEventType() {
        return eventType;
    }

}
