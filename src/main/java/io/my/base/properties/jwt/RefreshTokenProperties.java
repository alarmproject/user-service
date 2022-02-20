package io.my.base.properties.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter @Component
@ConfigurationProperties("jjwt.refresh-token")
public class RefreshTokenProperties {
    private int expiredTime;
    private String secretKey;
}
