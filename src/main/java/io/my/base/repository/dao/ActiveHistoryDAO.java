package io.my.base.repository.dao;

import io.my.active.payload.response.ActiveHistoryResponse;
import io.my.base.properties.ServerProperties;
import io.my.base.repository.query.ActiveHistoryQuery;
import io.my.base.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class ActiveHistoryDAO {
    private final DateUtil dateUtil;
    private final ActiveHistoryQuery activeHistoryQuery;
    private final ServerProperties serverProperties;

    public Flux<ActiveHistoryResponse> findActiveHistoryPaging(Long id, Long userId, Integer limit) {
        return this.activeHistoryQuery.findActiveHistoryPaging(id, userId, limit)
                .map((row, rowMetadata) -> {
                    String imageUrl = row.get("file_name", String.class);

                    if (imageUrl != null)
                        imageUrl = serverProperties.getImageUrl() +
                                serverProperties.getImagePath() +
                                imageUrl;

                    return ActiveHistoryResponse.builder()
                            .id(row.get("id", Long.class))
                            .content(row.get("content", String.class))
                            .friendsUserId(row.get("friends_user_id", Long.class))
                            .regDateTime(
                                    dateUtil.localDateTimeToUnixTime(
                                            row.get("reg_date_time", LocalDateTime.class)
                                    ))
                            .modDateTime(
                                    dateUtil.localDateTimeToUnixTime(
                                            row.get("mod_date_time", LocalDateTime.class)
                                    ))
                            .imageUrl(imageUrl)
                            .isFollow(row.get("friends_id", Long.class) != null)
                            .build();
                }).all()
        ;
    }
}
