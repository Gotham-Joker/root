package com.ht.authorization;

public interface Permission {
    public String getId();
    public String getName();
    public String getMethod();
    public String getUrl();
}
