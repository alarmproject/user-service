package io.my.base.repository.dao;

import io.my.base.entity.Image;
import io.my.base.entity.User;
import io.my.base.properties.ServerProperties;
import io.my.base.repository.query.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class UserDAO {
    private final UserQuery userQuery;
    private final ServerProperties serverProperties;

    public Flux<User> findUserByName(String name) {
        return findUserSearch(this.userQuery.findUserByName(name));
    }

    public Flux<User> findUserByNickname(String nickname) {
        return findUserSearch(this.userQuery.findUserByNickname(nickname));
    }

    private Flux<User> findUserSearch(DatabaseClient.GenericExecuteSpec spec) {
        return spec.map((row, rowMetadata) -> {
            User user = new User();
            user.setName(row.get("name", String.class));
            user.setId(row.get("id", Long.class));
            user.setNickname(row.get("nickname", String.class));
            user.setEmail(row.get("email", String.class));

            if (row.get("file_name", String.class) != null) {
                Image image = new Image();
                image.setFileName(row.get("file_name", String.class));

                user.setImage(image);
            }

            return user;
        }).all();
    }

    public Mono<String> findUserImage(Long id) {
        return this.userQuery.findUserImage(id).map((row, rowMetadata) -> {
            String imageLink = row.get("file_name", String.class);
            if (imageLink != null) return serverProperties.getImageUrl() + "?fileName=" +imageLink;
            return "";
        }).one();
    }
}
