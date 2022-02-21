package io.my.user;

import io.my.base.annotation.Logger;
import io.my.base.payload.BaseResponse;
import io.my.user.payload.request.FindEmailRequest;
import io.my.user.payload.request.JoinRequest;
import io.my.user.payload.request.LoginRequest;
import io.my.user.payload.response.FindEmailResponse;
import io.my.user.payload.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mockito.internal.matchers.Find;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public Mono<LoginResponse> login(
            @RequestBody LoginRequest requestBody) {
        return userService.login(requestBody);
    }

    @PostMapping("/join")
    public Mono<LoginResponse> join(
            @RequestBody JoinRequest requestBody) {
        return userService.join(requestBody);
    }

    @Logger
    @GetMapping("/social/login")
    public Mono<LoginResponse> socialLogin(@RequestParam(value = "email") String email) {
        return userService.socialLogin(email);
    }

    @Logger
    @PostMapping("/social/join")
    public Mono<LoginResponse> socialJoin(@RequestBody JoinRequest requesetBody) {
        return userService.socialJoin(requesetBody);
    }

    @Logger
    @PostMapping("/find/email")
    public Mono<BaseResponse> registFindEmail(@RequestBody FindEmailRequest requestBody) {
        return userService.registFindEmail(requestBody.getEmail());
    }

    @Logger
    @GetMapping("/find/email")
    public Mono<FindEmailResponse> findEmail(@RequestParam("email") String email) {
        return userService.findEmail(email);
    }

    @Logger
    @PatchMapping("/change/password")
    public Mono<BaseResponse> changePassword(@RequestBody LoginRequest requestBody) {
        return userService.changePassword(requestBody.getEmail(), requestBody.getPassword());
    }


}
