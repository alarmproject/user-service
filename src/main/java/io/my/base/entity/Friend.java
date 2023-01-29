package io.my.base.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

@Getter @Setter
@Table("friend")
public class Friend extends BaseEntity {
    @Id
    private Long id;
    private Long userId;
    private Long followUserId;

    @Transient
    private User user;
    @Transient
    private User followUser;
}
