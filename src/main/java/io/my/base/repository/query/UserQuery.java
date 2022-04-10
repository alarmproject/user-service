package io.my.base.repository.query;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserQuery {
    private final DatabaseClient client;

    public DatabaseClient.GenericExecuteSpec findUserByName(String name, Long collegeId) {
        String query =
                "SELECT " +
                "u.id, u.name, u.nickname, u.email, i.file_name " +
                "FROM " +
                "user as u " +
                "LEFT JOIN image as i " +
                "ON u.image_id = i.id " +
                "WHERE " +
                (collegeId != null ? "u.college_id = :collegeId and " : "") +
                "u.name LIKE CONCAT('%', :name, '%')"
                ;
        DatabaseClient.GenericExecuteSpec sql = client.sql(query).bind("name", name);

        if (collegeId != null) sql = sql.bind("collegeId", collegeId);
        return sql;
    }

    public DatabaseClient.GenericExecuteSpec findUserByNickname(String nickname, Long collegeId) {
        String query =
                "SELECT " +
                "u.id, u.name, u.nickname, u.email, i.file_name " +
                "FROM " +
                "user as u " +
                "LEFT JOIN image as i " +
                "ON u.image_id = i.id " +
                "WHERE " +
                (collegeId != null ? "u.college_id = :collegeId and " : "") +
                "u.nickname LIKE CONCAT('%', :nickname, '%')"
                ;
        DatabaseClient.GenericExecuteSpec sql = client.sql(query).bind("nickname", nickname);

        if (collegeId != null) sql = sql.bind("collegeId", collegeId);
        return sql;
    }

    public DatabaseClient.GenericExecuteSpec findUserImage(Long id) {
        String query = "select i.file_name from `user` u left join image i on u.image_id = i.id where u.id = :id"
                ;
        return client.sql(query).bind("id", id);
    }

    public DatabaseClient.GenericExecuteSpec findUserInfo(Long id) {
        String query =
                "select " +
                "u.name " +
                ", u.nickname " +
                ", u.email " +
                ", u.class_of " +
                ", i.file_name " +
                ", c.name as college_name " +
                ", i.id as image_id " +
                "from " +
                "`user` u " +
                "left join image i on u.image_id = i.id " +
                "left join college c ON u.college_id = c.id " +
                "where u.id = :id"
                ;
        return client.sql(query).bind("id", id);
    }
}
