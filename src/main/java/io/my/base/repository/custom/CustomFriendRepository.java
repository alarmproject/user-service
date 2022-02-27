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
        String query =
                "SELECT " +
                "f.id as id " +
                ", u.id as follow_user_id " +
                ", u.email as follow_user_email " +
                ", u.name as follow_user_name " +
                ", u.nickname as follow_user_nickname " +
                ", i.id as follow_user_image_id " +
                ", i.file_name as follow_user_image_name " +
                "FROM " +
                "friend f " +
                "LEFT JOIN `user` u ON f.follow_user_id = u.id " +
                "LEFT JOIN image i ON u.image_id = i.id " +
                "WHERE user_id = :userId";

        return client.sql(query).bind("userId", userId).map((row, rowMetadata) -> {

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
