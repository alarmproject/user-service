package io.my.base.util;

import io.my.base.properties.jwt.AccessTokenProperties;
import io.my.base.properties.jwt.RefreshTokenProperties;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest extends Mockito {
    private final JwtUtil jwtUtil;

    public JwtUtilTest() {
        AccessTokenProperties accessTokenProperties = new AccessTokenProperties();
        accessTokenProperties.setExpiredTime(720);
        accessTokenProperties.setSecretKey("access_secret");
        RefreshTokenProperties refreshTokenProperties = new RefreshTokenProperties();
        refreshTokenProperties.setExpiredTime(1440);
        refreshTokenProperties.setSecretKey("refresh_secret");
        this.jwtUtil = new JwtUtil(accessTokenProperties, refreshTokenProperties);
    }

    @Test
    void createAccessTokenTest() {
        String jwt = jwtUtil.createAccessToken(2L);
        assertTrue(jwtUtil.verifyAccessToken(jwt));
    }

    @Test
    void createRefreshTokenTest() {
        String jwt = jwtUtil.createRefreshToken(2L);
        assertTrue(jwtUtil.verifyRefreshToken(jwt));
    }

}