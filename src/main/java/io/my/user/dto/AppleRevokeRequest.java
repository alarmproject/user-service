package io.my.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AppleRevokeRequest {
    private final String clientId;
    private final String clientSecret;
    private final String token;
    private final String tokenTypeHint;
}
