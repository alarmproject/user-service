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
                "* " +
                "FROM " +
                "active_history " +
                "WHERE " +
                ((id != null && id != 0) ? ("id < :id AND ") : "") +
                "user_id = :userId " +
                "ORDER BY id DESC LIMIT :limit";

        DatabaseClient.GenericExecuteSpec sql = client.sql(query);
        if (id != null && id != 0) sql = sql.bind("id", id);

        return sql
                .bind("userId", userId)
                .bind("limit", limit);
    }
}
