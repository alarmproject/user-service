package io.my.base.repository.query;

import io.my.base.entity.Friend;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class FriendQuery {
    private final DatabaseClient client;

    public DatabaseClient.GenericExecuteSpec findFriendListByUserId(Long userId) {
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

        return client.sql(query).bind("userId", userId);
    }

    public DatabaseClient.GenericExecuteSpec findFriendsByName(Long userId, String name) {
        String query = "" +
                "select " +
                "u.id, u.name, u.nickname, u.email, i.file_name " +
                "from " +
                "friend f " +
                "JOIN `user` u ON f.follow_user_id = u.id " +
                "LEFT JOIN image i ON u.image_id = i.id " +
                "where f.user_id = :userId and u.name LIKE CONCAT('%', :name, '%')"
                ;
        return client.sql(query).bind("userId", userId).bind("name", name);
    }

    public DatabaseClient.GenericExecuteSpec findFriendsByNickname(Long userId, String nickname) {
        String query = "" +
                "select " +
                "u.id, u.name, u.nickname, u.email, i.file_name " +
                "from " +
                "friend f " +
                "JOIN `user` u ON f.follow_user_id = u.id " +
                "LEFT JOIN image i ON u.image_id = i.id " +
                "where f.user_id = :userId and u.nickname LIKE CONCAT('%', :nickname, '%')"
                ;
        return client.sql(query).bind("userId", userId).bind("nickname", nickname);
    }
}
