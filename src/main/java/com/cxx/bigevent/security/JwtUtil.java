package com.cxx.bigevent.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private static final String KEY = System.getenv("JWT_SECRET_KEY");
    private static final String DEFAULT_KEY = "itheima_fallback_key_do_not_use_in_production";

    private static String getKey() {
        return KEY != null && !KEY.isEmpty() ? KEY : DEFAULT_KEY;
    }
	
    public static String genToken(Map<String, Object> claims) {
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 6))
                .sign(Algorithm.HMAC256(getKey()));
    }

    public static Map<String, Object> parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(getKey()))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
    }

}
