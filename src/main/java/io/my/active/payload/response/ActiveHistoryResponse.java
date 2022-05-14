package io.my.active.payload.response;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActiveHistoryResponse {
    private Long id;
    private String content;
    private Long regDateTime;
    private Long modDateTime;
    private Long friendsUserId;
    private Boolean isFollow;
}
