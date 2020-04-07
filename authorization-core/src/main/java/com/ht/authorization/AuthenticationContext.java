package com.ht.authorization;

public class AuthenticationContext {
    private static final InheritableThreadLocal<Authentication> holder = new InheritableThreadLocal<>();

    public static Authentication current() {
        return holder.get();
    }

    public static void setAuthentication(Authentication authentication) {
        holder.set(authentication);
    }

    public static void clearContext() {
        holder.remove();
    }

}
