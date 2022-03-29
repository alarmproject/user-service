package io.my.base.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Builder
@Table("image")
@NoArgsConstructor
@AllArgsConstructor
public class Image extends BaseEntity {
    @Id
    private Long id;
    private String fileName;
    private String link;
}
