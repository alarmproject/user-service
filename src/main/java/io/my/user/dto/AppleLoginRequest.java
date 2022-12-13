package io.my.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AppleLoginRequest {
    private final String clientId;
    private final String clientSecret;
    private final String code;
    private final String grantType;
    private final String refreshToken;
    private final String redirectUri;
}
