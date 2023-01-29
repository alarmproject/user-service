package io.my.base.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Builder
@Table("department")
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    private Long id;
    private Long collegeId;
    private String name;
}
