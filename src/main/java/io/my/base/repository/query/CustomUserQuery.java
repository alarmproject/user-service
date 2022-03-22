package io.my.base.repository.query;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomUserQuery {
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

}
