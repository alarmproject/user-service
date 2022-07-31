package io.my.friend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SearchFriendsResponse {
    private Long id;
    private String nickname;
    private String name;
    private String email;
    private String imageUrl;
}
