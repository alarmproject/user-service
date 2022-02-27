package io.my.base.repository.custom;

import io.my.base.entity.Image;
import io.my.base.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class CustomUserRepository {
    private final DatabaseClient client;

    public Flux<User> findUserByName(String name) {
        String query =
                "SELECT " +
                "u.id, u.name, u.nickname, u.email, i.file_name " +
                "FROM " +
                "user as u " +
                "LEFT JOIN image as i " +
                "ON u.image_id = i.id " +
                "WHERE u.name LIKE CONCAT('%', :name, '%')"
        ;
        return findUserSearch("name", name, query);
    }

    public Flux<User> findUserByNickname(String nickname) {
        String query =
                "SELECT " +
                        "u.id, u.name, u.nickname, u.email, i.file_name " +
                        "FROM " +
                        "user as u " +
                        "LEFT JOIN image as i " +
                        "ON u.image_id = i.id " +
                        "WHERE u.nickname LIKE CONCAT('%', :nickname, '%')"
                ;
        return findUserSearch("nickname", nickname, query);
    }

    private Flux<User> findUserSearch(String searchName, String search, String query) {
        return client.sql(query).bind(searchName, search).map((row, rowMetadata) -> {
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
}
