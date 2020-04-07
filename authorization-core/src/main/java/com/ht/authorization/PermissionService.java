package com.ht.authorization;

import java.util.List;

public interface PermissionService {
    List<? extends Permission> findAll();
}
