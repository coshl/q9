package com.summer.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    static final String SECRET = "ThisIsMWWYSecret";

    public static String generateToken(String username, Long userId) {
        HashMap<String, Object> map = new HashMap<>();
        //you can put any data in the map
        map.put("username", username);
        if (userId != null) {
            map.put("userId", userId);
        }
        String jwt = Jwts.builder()
                .setClaims(map)
                .setExpiration(new Date(System.currentTimeMillis() + 600_000L))// 10min
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return "MWWY " + jwt; //jwt前面一般都会加Bearer
    }

    public static Map<String, Object> validateToken(String token) {
        Map<String, Object> body = new HashMap<>();
        try {
            // parse the token.
            body = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace("MWWY ", ""))
                    .getBody();
        } catch (Exception e) {
            throw new IllegalStateException("Invalid Token. " + e.getMessage());
        }
        return body;
    }
}