package io.my.friend;

import io.my.base.annotation.Logger;
import io.my.base.payload.BaseExtentionResponse;
import io.my.base.payload.BaseResponse;
import io.my.friend.payload.response.FriendListResponse;
import io.my.friend.payload.response.SearchFriendsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @Logger
    @GetMapping
    public Mono<BaseExtentionResponse<List<FriendListResponse>>> getFriendsList() {
        return friendService.getFriends();
    }

    @Logger
    @PostMapping
    public Mono<BaseResponse> addFriend(Long id){
        return friendService.addFriend(id);
    }

    @Logger
    @DeleteMapping
    public Mono<BaseResponse> removeFriend(Long id) {
        return friendService.removeFriend(id);
    }

    @Logger
    @GetMapping("/search")
    public Mono<BaseExtentionResponse<List<SearchFriendsResponse>>> searchFriends(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "name", required = false, defaultValue = "") String name) {
        return friendService.searchFriends(id, name);
    }
}
