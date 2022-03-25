package io.my.mail;

import io.my.base.payload.BaseExtentionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @GetMapping("/code")
    public Mono<BaseExtentionResponse<Integer>> sendCode(
            @RequestParam("email") String email) {
        int mailCode = new Random().nextInt(900) + 100;

        Mono.fromCallable(() -> this.mailService.sendJoinCodeMail(email, mailCode))
                .subscribeOn(Schedulers.boundedElastic()).subscribe();

        return Mono.just(new BaseExtentionResponse<>(mailCode));
    }
}
