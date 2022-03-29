package io.my.professor.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaveProfessorRequest {
    private Long departmentId;
    private String name;
    private Long imageId;
}
