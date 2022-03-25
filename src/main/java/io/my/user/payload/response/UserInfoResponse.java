package io.my.user.payload.response;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private String nickname;
    private String name;
    private String email;
    private Integer classOf;
    private String imageUrl;
    private String collegeName;
}
