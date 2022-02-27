package io.my.friend;

import io.my.base.payload.BaseExtentionResponse;
import io.my.base.payload.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @GetMapping
    public BaseExtentionResponse<Object> getFriendsList() {



        return new BaseExtentionResponse<>(null);
    }
}
