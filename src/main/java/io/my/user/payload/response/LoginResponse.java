package io.my.user.payload.response;

import io.my.base.payload.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse extends BaseResponse {
    private Long id;
    private String authorization;
}
