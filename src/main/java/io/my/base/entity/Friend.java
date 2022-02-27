package io.my.base.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter @Setter
@Table("friend")
public class Friend extends BaseEntity {
    @Id
    private Long id;

    private User user;
    private User followUser;
}
