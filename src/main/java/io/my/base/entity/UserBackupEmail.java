package io.my.base.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter @Setter
@Table("user_backup_email")
public class UserBackupEmail {
    @Id
    private Long id;
    private String email;
    private Long userId;
}
