package io.my.base.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("college_email_request")
public class CollegeEmailRequest {
    @Id
    private Long id;
    private String name;
    private String email;
}
