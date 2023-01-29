package io.my.base.repository.query;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DualQuery {
    private final DatabaseClient client;

    public DatabaseClient.GenericExecuteSpec callDbTest() {
        String query ="select :callDbTest as callDbTest from dual";
        return client.sql(query).bind("callDbTest", true);
    }
}
