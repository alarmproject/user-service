package io.my.user.payload.response;

import io.my.base.payload.BaseResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindEmailResponse extends BaseResponse {
    private String email;
}
