package io.my.base.repository.custom;

import io.my.base.entity.Friend;
import io.my.base.entity.Image;
import io.my.base.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class CustomFriendRepository {
    private final DatabaseClient client;

    public Flux<Friend> findFriendListByUserId(Long userId) {
        String query = "";

        return client.sql(query).bind("userId", userId).map((row, rowMetadata) -> {

            Friend friend = new Friend();
            User user = new User();
            user.setName(row.get("followUserName", String.class));
            user.setEmail(row.get("followUserEmail", String.class));
            user.setNickname(row.get("followUserNickname", String.class));

            if (row.get("follow_file_name", String.class) != null) {
                Image image = new Image();
                image.setFileName(row.get("follow_file_name", String.class));
                user.setImage(image);
            }

            friend.setFollowUser(user);

            return friend;
        }).all();
    }
}
