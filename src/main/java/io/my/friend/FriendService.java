package io.my.friend;

import io.my.active.ActiveContent;
import io.my.base.context.JwtContextHolder;
import io.my.base.entity.ActiveHistory;
import io.my.base.entity.Friend;
import io.my.base.payload.BaseExtentionResponse;
import io.my.base.payload.BaseResponse;
import io.my.base.properties.ServerProperties;
import io.my.base.repository.ActiveHistoryRepository;
import io.my.base.repository.FriendRepository;
import io.my.base.repository.UserRepository;
import io.my.base.repository.dao.FriendDAO;
import io.my.base.repository.dao.UserDAO;
import io.my.friend.payload.response.FriendListResponse;
import io.my.friend.payload.response.SearchFriendsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final ActiveHistoryRepository activeHistoryRepository;
    private final FriendDAO friendDAO;
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final UserDAO userDAO;

    private final ServerProperties serverProperties;

    public Mono<BaseExtentionResponse<List<FriendListResponse>>>  getFriends() {
        return JwtContextHolder.getMonoUserId()
                .flatMap(userId -> friendDAO.findFriendListByUserId(userId).collectList())
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
                        serverProperties.getImageUrl() +
                                serverProperties.getImagePath() +
                                friend.getFollowUser().getImage().getFileName()
                );
                response.setUserImageId(friend.getFollowUser().getImage().getId());
            }

            result.add(response);
        });
        return result;
    }

    public Mono<BaseResponse> addFriend(Long id) {
        AtomicLong atomicUserId = new AtomicLong();

        return JwtContextHolder.getMonoUserId()
            .flatMap(userId -> {
                atomicUserId.set(userId);
                Friend entity = new Friend();
                entity.setUserId(userId);
                entity.setFollowUserId(id);
                return friendRepository.save(entity);
            })
            .flatMap(friend -> userRepository.findById(friend.getUserId()))
            .flatMap(user -> {
                ActiveHistory entity = new ActiveHistory();
                entity.setUserId(user.getId());
                entity.setFriendsUserId(id);
                String content = ActiveContent.ADDED_FRIEND.getContent();
                entity.setContent(content.replaceFirst("\\{}", user.getName()));

                return activeHistoryRepository.save(entity);
            })
            .flatMap(activeHistory -> userRepository.findById(id))
            .flatMap(user -> {
                ActiveHistory entity = new ActiveHistory();
                entity.setUserId(user.getId());
                entity.setFriendsUserId(atomicUserId.get());
                String content = ActiveContent.BE_ADDED_FRIEND.getContent();
                entity.setContent(content.replaceFirst("\\{}", user.getName()));

                return activeHistoryRepository.save(entity);
            })
            .map(entity -> new BaseResponse())
            .onErrorReturn(new BaseResponse(1, "등록 실패"))
        ;
    }

    public Mono<BaseResponse> removeFriend(Long id) {
        AtomicReference<Long> userIdLong = new AtomicReference<>(0L);

        return JwtContextHolder.getMonoUserId()
            .flatMap(userId -> {
                userIdLong.set(userId);
                return friendRepository.deleteByUserIdAndFollowUserId(userId, id);
            })
            .then(userRepository.findById(id))
            .flatMap(user -> {
                ActiveHistory entity = new ActiveHistory();
                entity.setUserId(userIdLong.get());
                entity.setContent(ActiveContent.DELETED_FRIEND.getContent().replaceAll("\\{}", user.getName()));
                return activeHistoryRepository.save(entity);
            })
            .map(entity -> new BaseResponse())
            .switchIfEmpty(Mono.just(new BaseResponse()))
        ;
    }

    public Mono<BaseExtentionResponse<List<SearchFriendsResponse>>> searchFriends(Long id, String name) {
        return JwtContextHolder.getMonoUserId().flatMapMany(userId -> userDAO.findAllNotFriends(userId, id, name))
                .collectList()
                .map(BaseExtentionResponse::new)
                ;
    }
}
