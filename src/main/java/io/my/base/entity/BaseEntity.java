package io.my.base.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class BaseEntity {
    private Long id;
    private LocalDateTime regDateTime;
    private LocalDateTime modDateTime;

    public BaseEntity() {
        this.regDateTime = LocalDateTime.now();
        this.modDateTime = regDateTime;
    }
}
