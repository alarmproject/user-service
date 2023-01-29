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
    private Integer classOf;

    private String deviceToken;
    private String collegeEmail;
    private Long collegeId;
    private Long imageId;

    public Integer getId() { return null; }
}
