package io.my.user.payload.response.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchUser {
    private Long id;
    private String nickname;
    private String name;
    private String email;
    private String fileName;
}
