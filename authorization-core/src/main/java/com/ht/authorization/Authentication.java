package com.ht.authorization;

import java.util.List;
import java.util.Map;

public interface Authentication {
    String getId();

    List<? extends Permission> getPermissions();

    <T> T getAttribute(String key);
}
