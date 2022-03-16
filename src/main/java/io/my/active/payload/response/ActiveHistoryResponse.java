package io.my.active.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ActiveHistoryResponse {
    private Long id;
    private String content;
    private Long regDateTime;
    private Long modDateTime;
}
