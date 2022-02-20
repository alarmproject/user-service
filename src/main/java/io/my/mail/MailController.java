package io.my.mail;

import io.my.mail.payload.MailCodeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @GetMapping("/code")
    public Mono<MailCodeResponse> sendCode(
            @RequestParam("email") String email) {
        int mailCode = new Random().nextInt(900) + 100;

        return mailService.sendJoinCodeMail(email, mailCode);
    }
}
