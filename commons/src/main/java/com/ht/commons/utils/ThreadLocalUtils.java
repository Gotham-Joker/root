
package com.ht.commons.utils;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalUtils {

    private ThreadLocalUtils() {
    }

    private static final ThreadLocal<Map<String, Object>> local = ThreadLocal.withInitial(HashMap::new);


    public static Map<String, Object> getAll() {
        return local.get();
    }


    public static <T> T put(String key, T value) {
        local.get().put(key, value);
        return value;
    }

    public static void remove(String key) {
        local.get().remove(key);
    }

    public static void clear() {
        local.remove();
    }

    public static <T> T get(String key) {
        return ((T) local.get().get(key));
    }

}
