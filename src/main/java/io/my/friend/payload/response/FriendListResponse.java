package io.my.friend.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FriendListResponse {
    private Long id;
    private Long userId;
    private Long userImageId;

    private String nickname;
    private String name;
    private String email;
    private String userImageUrl;
}
