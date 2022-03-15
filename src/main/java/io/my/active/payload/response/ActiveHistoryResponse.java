package io.my.active.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ActiveHistoryResponse {
    private Long id;
    private String content;
    private LocalDateTime regDateTime;
    private LocalDateTime modDateTime;
}
