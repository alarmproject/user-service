package io.my.base.repository.dao;

import io.my.base.entity.Friend;
import io.my.base.entity.Image;
import io.my.base.entity.User;
import io.my.base.repository.query.FriendQuery;
import lombok.RequiredArgsConstructor;
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
                        Image image = new Image();
                        image.setId(row.get("follow_user_image_id", Long.class));
                        image.setFileName(row.get("follow_user_image_name", String.class));
                        user.setImage(image);
                    }

                    friend.setFollowUser(user);

                    return friend;
        }).all();
    }
}
