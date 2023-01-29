package io.my.user.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserResponse {
    private Long id;
    private String nickname;
    private String name;
    private String email;
    private String fileName;
}
