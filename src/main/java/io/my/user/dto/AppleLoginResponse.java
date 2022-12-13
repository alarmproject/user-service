package io.my.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AppleLoginResponse {
    private final String accessToken;
    private final Long expiresIn;
    private final String idToken;
    private final String refreshToken;
    private final String tokenType;
}
