package io.my.base.repository.dao;

import io.my.base.entity.Image;
import io.my.base.entity.User;
import io.my.base.properties.ServerProperties;
import io.my.base.repository.query.UserQuery;
import io.my.user.payload.response.UserInfoResponse;
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

    public Flux<User> findUserByName(String name, Long collegeId) {
        return findUserSearch(this.userQuery.findUserByName(name, collegeId));
    }

    public Flux<User> findUserByNickname(String nickname, Long collegeId) {
        return findUserSearch(this.userQuery.findUserByNickname(nickname, collegeId));
    }

    private Flux<User> findUserSearch(DatabaseClient.GenericExecuteSpec spec) {
        return spec.map((row, rowMetadata) -> {
            User user = new User();
            user.setName(row.get("name", String.class));
            user.setId(row.get("id", Long.class));
            user.setNickname(row.get("nickname", String.class));
            user.setEmail(row.get("email", String.class));

            if (row.get("file_name", String.class) != null) {
                Image image = Image.builder().fileName(row.get("file_name", String.class)).build();

                user.setImage(image);
            }

            return user;
        }).all();
    }

    public Mono<String> findUserImage(Long id) {
        return this.userQuery.findUserImage(id).map((row, rowMetadata) -> {
            String imageLink = row.get("file_name", String.class);
            if (imageLink != null) return serverProperties.getImageUrl() +
                    serverProperties.getImagePath() +
                    imageLink;
            return "";
        }).one();
    }

    public Mono<UserInfoResponse> findUserInfo(Long id) {
        return this.userQuery.findUserInfo(id).map((row, rowMetadata) -> {
            String imageUrl = row.get("file_name", String.class);

            if (imageUrl != null)
                imageUrl = serverProperties.getImageUrl() +
                        serverProperties.getImagePath() +
                        imageUrl;

            return UserInfoResponse.builder()
                    .nickname(row.get("nickname", String.class))
                    .name(row.get("name", String.class))
                    .email(row.get("email", String.class))
                    .collegeEmail(row.get("college_email", String.class))
                    .classOf(row.get("class_of", Integer.class))
                    .imageId(row.get("image_id", Long.class))
                    .collegeId(row.get("college_id", Long.class))
                    .collegeName(row.get("college_name", String.class))
                    .friendsCount(row.get("friends_count", Integer.class))
                    .imageUrl(imageUrl)
                    .build();
        }).one();
    }
}
