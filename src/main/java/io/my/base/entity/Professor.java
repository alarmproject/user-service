package io.my.base.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("professor")
public class Professor {
    @Id
    private Long id;
    private Long collegeId;
    private String name;
    private Long departmentId;
    private Long imageId;

    @Transient
    private Department department;
    @Transient
    private Image image;
}
