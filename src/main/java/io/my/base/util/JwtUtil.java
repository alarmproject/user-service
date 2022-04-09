package io.my.base.util;

import io.jsonwebtoken.*;
import io.my.base.properties.jwt.AccessTokenProperties;
import io.my.base.properties.jwt.RefreshTokenProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final AccessTokenProperties accessTokenProperties;
    private final RefreshTokenProperties refreshTokenProperties;

    public String createAccessToken(Long userId) {
        return createToken(
                userId,
                accessTokenProperties.getSecretKey(),
                accessTokenProperties.getExpiredTime());
    }

    public String createRefreshToken(Long userId) {
        return createToken(
                userId,
                refreshTokenProperties.getSecretKey(),
                refreshTokenProperties.getExpiredTime());
    }

    private String createToken(Long userId, String secretKey, int expiredTime) {
        return createToken(
                userId,
                getKey(secretKey),
                Timestamp.valueOf(LocalDateTime.now().plusHours(expiredTime)))
                ;
    }

    private String createToken(Long userId, Key key, Timestamp expiredTime) {
        String subject = "user";
        Map<String, Object> headers = new HashMap<>();
        Map<String, Object> payloads = new HashMap<>();

        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        payloads.put("id", userId);

        return Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setSubject(subject)
                .setExpiration(expiredTime)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    public Long getUserIdByAccessToken(String jwt) {
        return Long.valueOf(parseAccessToken(jwt).get("id").toString());
    }

    public Claims parseAccessToken(String jwt) {
        return parseJwt(jwt, getKey(accessTokenProperties.getSecretKey()));
    }

    public boolean verifyAccessToken(String jwt) {
        Claims claims = parseAccessToken(jwt);

        if (claims == null) {
            return false;
        }

        return claims.getExpiration().after(new Date());
    }

    public boolean verifyRefreshToken(String jwt) {
        return parseRefreshToken(jwt).getExpiration().after(new Date());
    }

    public Claims parseRefreshToken(String jwt) {
        return parseJwt(jwt, getKey(refreshTokenProperties.getSecretKey()));
    }

    private Claims parseJwt(String jwt, Key key) {
        try {
            return Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch(SignatureException | ExpiredJwtException e) {
            return null;
        }
    }

    private Key getKey(String secretKey) {
        byte[] secretBytes = Base64.getEncoder().encode(secretKey.getBytes());
        return new SecretKeySpec(secretBytes, SignatureAlgorithm.HS512.getJcaName());
    }

    /**
     * ReactiveJwtContextHolder에서 jwt 정보를 가져와서 userId 반환.
     */
//    public Mono<String> getMonoUserId() {
//        return JwtContextHolder.getContext()
//                .flatMap(context -> {
//                    String userId = context.getUserId();
//                    if (userId == null) {
//                        return context.getJwt().map(this::getUserIdByAccessToken);
//                    } else {
//                        return Mono.just(userId);
//                    }
//                });
//    }

    public Claims parseJwt(String jwt, String accessToken) {
        return Jwts.parser()
                .setSigningKey(accessToken)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
