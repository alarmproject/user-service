package io.my.user;

import io.my.base.annotation.Logger;
import io.my.base.payload.BaseExtentionResponse;
import io.my.base.payload.BaseResponse;
import io.my.user.payload.request.FindEmailRequest;
import io.my.user.payload.request.JoinRequest;
import io.my.user.payload.request.LoginRequest;
import io.my.user.payload.request.PatchUserPasswordRequest;
import io.my.user.payload.response.LoginResponse;
import io.my.user.payload.response.SearchUserResponse;
import io.my.user.payload.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public Mono<BaseExtentionResponse<LoginResponse>> login(
            @RequestBody LoginRequest requestBody) {
        return userService.login(requestBody);
    }

    @PostMapping("/join")
    public Mono<BaseExtentionResponse<LoginResponse>> join(
            @RequestBody JoinRequest requestBody) {
        return userService.join(requestBody);
    }

    @Logger
    @GetMapping("/social/login")
    public Mono<BaseExtentionResponse<LoginResponse>> socialLogin(@RequestParam(value = "email") String email) {
        return userService.socialLogin(email);
    }

    @Logger
    @PostMapping("/social/join")
    public Mono<BaseExtentionResponse<LoginResponse>> socialJoin(@RequestBody JoinRequest requesetBody) {
        return userService.socialJoin(requesetBody);
    }

    @Logger
    @PostMapping("/find/email")
    public Mono<BaseResponse> registFindEmail(@RequestBody FindEmailRequest requestBody) {
        return userService.registFindEmail(requestBody.getEmail());
    }

    @Logger
    @GetMapping("/find/email")
    public Mono<BaseExtentionResponse<String>> findEmail(@RequestParam("email") String email) {
        return userService.findEmail(email);
    }

    @Logger
    @GetMapping("/search")
    public Mono<BaseExtentionResponse<List<SearchUserResponse>>> searchUser(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "searchType", defaultValue = "0", required = false) int type,
            @RequestParam(name = "isSameCollege", defaultValue = "false", required = false) Boolean isSameCollege) {

        Mono<BaseExtentionResponse<List<SearchUserResponse>>> responseMono;

        if (type == 0) responseMono = userService.searchUserByName(isSameCollege, search);
        else responseMono = userService.searchUserByNickname(isSameCollege, search);

        return responseMono;
    }

    @Logger
    @PatchMapping("/password")
    public Mono<BaseResponse> changePassword(@RequestBody LoginRequest requestBody) {
        return userService.changePassword(requestBody.getEmail(), requestBody.getPassword());
    }

    @Logger
    @PatchMapping("/image")
    public Mono<BaseResponse> changeImage(@RequestParam("id") Long id) {
        return userService.changeImage(id);
    }

    @Logger
    @PatchMapping("/nickname")
    public Mono<BaseResponse> changeImage(@RequestParam("nickname") String nickname) {
        return userService.changeNickname(nickname);
    }

    @Logger
    @GetMapping("/check/email")
    public Mono<BaseExtentionResponse<Boolean>> checkEmail(@RequestParam("email") String email) {
        return userService.checkEmail(email);
    }

    @Logger
    @GetMapping("/check/nickname")
    public Mono<BaseExtentionResponse<Boolean>> checkNickname(@RequestParam("nickname") String nickname) {
        return userService.checkNickname(nickname);
    }

    @Logger
    @GetMapping("/image/{id}")
    public Mono<BaseExtentionResponse<String>> getImageLink(@PathVariable("id") Long userId) {
        return userService.getImageLink(userId);
    }
    
    @Logger
    @GetMapping("/{id}")
    public Mono<BaseExtentionResponse<UserInfoResponse>> getUserInfo(@PathVariable("id") Long userId) {
        return userService.getUserInfo(userId);
    }

    @Logger
    @PatchMapping("/change/password")
    public Mono<BaseResponse> changeUserPassword(
            @RequestBody PatchUserPasswordRequest requestBody) {
        return userService.changePassword(requestBody);
    }



}
