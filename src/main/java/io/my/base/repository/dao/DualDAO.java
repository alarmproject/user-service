package io.my.base.repository.dao;

import io.my.base.repository.query.DualQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class DualDAO {
    private final DualQuery dualQuery;

    public Mono<Boolean> callDbTest() {
        return this.dualQuery.callDbTest()
                .map((row, rowMetadata) ->
                        row.get("callDbTest", Boolean.class))
                .one()
                ;
    }
}
