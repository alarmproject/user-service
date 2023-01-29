package io.my.mail;

import io.my.mail.builder.MailBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.mail.*;
import javax.mail.internet.InternetAddress;

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

    private void sendMail(InternetAddress[] toAddr, String subject, String text) throws MessagingException {
        Transport.send(
                MailBuilder.builder()
                        .properties(this.mailProperties)
                        .from("universitycafeterialife")
                        .recipients(toAddr)
                        .subject(subject)
                        .content(text)
                        .build()
        );

    }
}
