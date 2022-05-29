package io.my.slack;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("slack")
public class SlackProperties {
    private String userOauthToken;
    private String botUserOauthToken;
    private String clientSecret;
    private String signingSecret;
    private String verificationToken;
}
