package io.my.user.payload.response;

import io.my.base.payload.BaseResponse;
import io.my.user.payload.response.dto.SearchUser;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SearchUserResponse extends BaseResponse {
    List<SearchUser> list;
}
