package io.my.mail;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("mail")
public class MailProperties {
    private String username;
    private String password;
    private String smtpHost;
    private String smtpPort;
    private String smtpAuth;
    private String smtpSocketFactoryPort;
    private String smtpSocketFactoryClass;
}
