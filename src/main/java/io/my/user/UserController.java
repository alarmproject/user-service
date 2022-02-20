package io.my.user;

import io.my.base.annotation.Logger;
import io.my.user.payload.request.JoinRequest;
import io.my.user.payload.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Logger
    @GetMapping("/login")
    public Mono<LoginResponse> login(@RequestParam(value = "email") String email) {
        return userService.login(email);
    }

    @Logger
    @PostMapping("/join")
    public Mono<LoginResponse> join(@RequestBody JoinRequest requesetBody) {
        return userService.join(requesetBody);
    }


}
