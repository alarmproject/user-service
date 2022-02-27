package io.my.friend;

import io.my.base.context.JwtContextHolder;
import io.my.base.entity.Friend;
import io.my.base.payload.BaseExtentionResponse;
import io.my.base.payload.BaseResponse;
import io.my.base.properties.ServerProperties;
import io.my.base.repository.FriendRepository;
import io.my.base.repository.custom.CustomFriendRepository;
import io.my.friend.payload.response.FriendListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final CustomFriendRepository customFriendRepository;
    private final FriendRepository friendRepository;
    private final ServerProperties serverProperties;

    public Mono<BaseExtentionResponse<List<FriendListResponse>>>  getFriends() {
        return JwtContextHolder.getMonoUserId()
                .flatMap(userId -> customFriendRepository.findFriendListByUserId(userId).collectList())
                .map(friendList -> {
                    List<FriendListResponse> list = changeEntityToResponse(friendList);
                    return new BaseExtentionResponse<>(list);
                })
                .switchIfEmpty(Mono.just(new BaseExtentionResponse<>(new ArrayList<>())))
                ;
    }

    private List<FriendListResponse> changeEntityToResponse(List<Friend> list) {
        List<FriendListResponse> result = new ArrayList<>();
        list.forEach(friend -> {
            FriendListResponse response = new FriendListResponse();
            response.setId(friend.getId());

            response.setUserId(friend.getFollowUser().getId());

            response.setEmail(friend.getFollowUser().getEmail());
            response.setName(friend.getFollowUser().getName());
            response.setNickname(friend.getFollowUser().getNickname());

            if (friend.getFollowUser().getImage() != null) {
                response.setUserImageUrl(
                        serverProperties.getImaegUrl() +
                                friend.getFollowUser().getImage().getFileName()
                );
                response.setUserImageId(friend.getFollowUser().getImage().getId());
            }

            result.add(response);
        });
        return result;
    }

    public Mono<BaseResponse> addFriend(Long id) {
        return JwtContextHolder.getMonoUserId()
            .flatMap(userId -> {
                Friend entity = new Friend();
                entity.setUserId(userId);
                entity.setFollowUserId(id);
                return friendRepository.save(entity);
            })
            .map(friend -> new BaseResponse())
            .switchIfEmpty(Mono.just(new BaseResponse(1, "등록 실패")))
        ;
    }

    public Mono<BaseResponse> removeFriend(Long id) {
        return JwtContextHolder.getMonoUserId()
            .flatMap(userId -> friendRepository.deleteByUserIdAndFollowUserId(userId, id))
            .map(o -> new BaseResponse())
            .switchIfEmpty(Mono.just(new BaseResponse()))
        ;
    }

}
