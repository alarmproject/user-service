package io.my.base.repository.custom;

import io.my.base.entity.ActiveHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class CustomActiveHistoryRepository {
    private final DatabaseClient client;

    public Flux<ActiveHistory> findActiveHistoryPaging(Long id, Long userId, Integer limit) {
        String query =
                "SELECT " +
                "* " +
                "FROM " +
                "active_history " +
                "WHERE " +
                "id < :id " +
                "AND user_id = :userId " +
                "ORDER BY id DESC LIMIT :limit"
                ;

        DatabaseClient.GenericExecuteSpec sql = client.sql(query);

        return sql
                .bind("id", id)
                .bind("userId", userId)
                .bind("limit", limit).map((row, rowMetadata) -> {
                    ActiveHistory entity = new ActiveHistory();

                    entity.setId(row.get("id", Long.class));
                    entity.setUserId(row.get("user_id", Long.class));
                    entity.setContent(row.get("content", String.class));
                    entity.setRegDateTime(row.get("reg_date_time", LocalDateTime.class));
                    entity.setModDateTime(row.get("mod_date_time", LocalDateTime.class));

                    return entity;
                }).all()
        ;
    }
}
