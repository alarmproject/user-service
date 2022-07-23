package io.my.base.repository.query;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActiveHistoryQuery {
    private final DatabaseClient client;

    public DatabaseClient.GenericExecuteSpec findActiveHistoryPaging(Long id, Long userId, Integer limit) {
        String query =
                "SELECT " +
                "ah.* " +
                ", f.id as friends_id " +
                ", i.file_name as file_name " +
                "FROM " +
                "active_history ah " +
                "left join friend f on ah.user_id = f.user_id and ah.friends_user_id = f.follow_user_id " +
                "join user u on ah.friends_user_id = u.id " +
                "left join image i on u.image_id = i.id " +
                "WHERE " +
                ((id != null && id != 0) ? ("ah.id < :id AND ") : "") +
                "ah.user_id = :userId " +
                "ORDER BY ah.id DESC LIMIT :limit";

        DatabaseClient.GenericExecuteSpec sql = client.sql(query);
        if (id != null && id != 0) sql = sql.bind("id", id);

        return sql
                .bind("userId", userId)
                .bind("limit", limit);
    }
}
