package io.my.user.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class JoinRequest {

    private Byte type;

    private String name;
    private String email;
    private String nickname;
    private String password;

    private String deviceToken;
    private String collegeEmail;
    private Long collegeId;

    public Integer getId() { return null; }
}
