package io.my.base.controller;

import io.my.base.annotation.Logger;
import io.my.base.context.JwtContextHolder;
import io.my.base.payload.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class HealthCheckController {

    @Logger
    @GetMapping("/healthcheck")
    public Mono<ResponseEntity<BaseResponse>> healthCheck() {
        return JwtContextHolder.getMonoContext().map(context -> {
            log.info("healthCheck!!");
            return ResponseEntity.ok(new BaseResponse("service is health"));
        });
    }
}
