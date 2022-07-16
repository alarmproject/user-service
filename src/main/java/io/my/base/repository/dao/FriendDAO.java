package io.my.base.repository.dao;

import io.my.base.entity.Friend;
import io.my.base.entity.Image;
import io.my.base.entity.User;
import io.my.base.repository.query.FriendQuery;
import io.my.user.payload.response.SearchUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class FriendDAO {
    private final FriendQuery friendQuery;

    public Flux<Friend> findFriendListByUserId(Long userId) {
        return this.friendQuery.findFriendListByUserId(userId)
                .map((row, rowMetadata) -> {
                    Friend friend = new Friend();
                    User user = new User();
                    friend.setId(row.get("id", Long.class));
                    user.setId(row.get("follow_user_id", Long.class));
                    user.setName(row.get("follow_user_name", String.class));
                    user.setEmail(row.get("follow_user_email", String.class));
                    user.setNickname(row.get("follow_user_nickname", String.class));

                    if (row.get("follow_user_image_name", String.class) != null) {
                        Image image = Image.builder()
                                .id(row.get("follow_user_image_id", Long.class))
                                .fileName(row.get("follow_user_image_name", String.class))
                                .build();
                        user.setImage(image);
                    }

                    friend.setFollowUser(user);

                    return friend;
        }).all();
    }

    public Flux<User> findFriendsByName(Long userId, String name) {
        return findUserSearch(this.friendQuery.findFriendsByName(userId, name));
    }

    public Flux<User> findFriendsByNickname(Long userId, String nickname) {
        return findUserSearch(this.friendQuery.findFriendsByNickname(userId, nickname));
    }

    private Flux<User> findUserSearch(DatabaseClient.GenericExecuteSpec spec) {
        return spec.map((row, rowMetadata) -> {
            User user = new User();
            user.setName(row.get("name", String.class));
            user.setId(row.get("id", Long.class));
            user.setNickname(row.get("nickname", String.class));
            user.setEmail(row.get("email", String.class));

            if (row.get("file_name", String.class) != null) {
                Image image = Image.builder().fileName(row.get("file_name", String.class)).build();

                user.setImage(image);
            }

            return user;
        }).all();
    }
}
