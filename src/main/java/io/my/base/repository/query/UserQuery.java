package io.my.base.repository.query;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQuery {
    private final DatabaseClient client;

    public DatabaseClient.GenericExecuteSpec findUserByName(String name) {
        String query =
                "SELECT " +
                "u.id, u.name, u.nickname, u.email, i.file_name " +
                "FROM " +
                "user as u " +
                "LEFT JOIN image as i " +
                "ON u.image_id = i.id " +
                "WHERE u.name LIKE CONCAT('%', :name, '%')"
                ;
        return client.sql(query).bind("name", name);
    }

    public DatabaseClient.GenericExecuteSpec findUserByNickname(String nickname) {
        String query =
                "SELECT " +
                "u.id, u.name, u.nickname, u.email, i.file_name " +
                "FROM " +
                "user as u " +
                "LEFT JOIN image as i " +
                "ON u.image_id = i.id " +
                "WHERE u.nickname LIKE CONCAT('%', :nickname, '%')"
                ;
        return client.sql(query).bind("nickname", nickname);
    }

    public DatabaseClient.GenericExecuteSpec findUserImage(Long id) {
        String query = "select i.file_name from `user` u left join image i on u.image_id = i.id where u.id = :id"
                ;
        return client.sql(query).bind("id", id);
    }

}
