package com.cxx.bigevent.util;

import java.util.Map;

public class UserContext {
    private static final String USER_ID_KEY = "id";
    private static final String USERNAME_KEY = "username";

    public static Integer getUserId() {
        Map<String, Object> map = ThreadLocalUtil.get();
        if (map == null) {
            return null;
        }
        Object userId = map.get(USER_ID_KEY);
        if (userId instanceof Number) {
            return ((Number) userId).intValue();
        }
        return null;
    }

    public static Long getUserIdAsLong() {
        Map<String, Object> map = ThreadLocalUtil.get();
        if (map == null) {
            return null;
        }
        Object userId = map.get(USER_ID_KEY);
        if (userId instanceof Number) {
            return ((Number) userId).longValue();
        }
        return null;
    }

    public static String getUsername() {
        Map<String, Object> map = ThreadLocalUtil.get();
        if (map == null) {
            return null;
        }
        return (String) map.get(USERNAME_KEY);
    }

    public static boolean isLoggedIn() {
        return getUserId() != null;
    }
}
