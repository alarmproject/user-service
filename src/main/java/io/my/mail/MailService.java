package io.my.mail;

import io.my.base.exception.object.MailSenderException;
import io.my.base.payload.BaseExtentionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MailProperties mailProperties;

    public Mono<Void> sendJoinCodeMail(String email, int mailCode) throws Exception {
        String subject = "학식라이프 인증코드입니다.";
        String text =
                "<div>" +
                    "<span>학식라이프 인증 코드는 " +
                        "<b>" +
                        mailCode +
                        "</b>" +
                    " 입니다.</span>" +
                "</div>" +
                "<div style='margin-top: 10px'>이 메일은 발신 전용 메일입니다.</div>";
        this.sendMail(email, subject, text);
        return Mono.empty();
    }

    private void sendMail(String email, String subject, String text) throws Exception {
        InternetAddress[] toAddr = new InternetAddress[1];
        toAddr[0] = new InternetAddress(email);
        this.sendMail(toAddr, subject, text);
    }

    private void sendMail(InternetAddress[] toAddr, String subject, String text) {
        String username = this.mailProperties.getUsername();
        String password = this.mailProperties.getPassword();

        Properties properties = new Properties();
        properties.put("mail.smtp.host", this.mailProperties.getSmtpHost());
        properties.put("mail.smtp.port", this.mailProperties.getSmtpPort());
        properties.put("mail.smtp.auth", this.mailProperties.getSmtpAuth());
        properties.put("mail.smtp.socketFactory.port", this.mailProperties.getSmtpSocketFactoryPort());
        properties.put("mail.smtp.socketFactory.class", this.mailProperties.getSmtpSocketFactoryClass());

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("universitycafeterialife"));

            message.setRecipients(
                    Message.RecipientType.TO,
                    toAddr
            );

            message.setSubject(subject);
            message.setContent(text, "text/html; charset=utf-8");
            Transport.send(message);
        } catch(MessagingException e) {
            throw new RuntimeException();
        }

    }
}
