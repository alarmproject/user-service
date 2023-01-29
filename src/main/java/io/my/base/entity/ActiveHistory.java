package io.my.base.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class ActiveHistory extends BaseEntity {
    @Id
    private Long id;
    private Long userId;
    private String content;
    private Long friendsUserId;
}
