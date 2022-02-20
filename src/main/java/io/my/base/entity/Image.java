package io.my.base.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter @Setter
@Table("image")
public class Image extends BaseEntity {
    @Id
    private Long id;
    private String fileName;
    private String link;
}
