package io.my.base.repository.dao;

import io.my.base.entity.ActiveHistory;
import io.my.base.repository.query.ActiveHistoryQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class ActiveHistoryDAO {
    private final ActiveHistoryQuery activeHistoryQuery;

    public Flux<ActiveHistory> findActiveHistoryPaging(Long id, Long userId, Integer limit) {
        return this.activeHistoryQuery.findActiveHistoryPaging(id, userId, limit)
                .map((row, rowMetadata) -> {
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
