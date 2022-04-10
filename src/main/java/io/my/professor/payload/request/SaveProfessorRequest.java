package io.my.professor.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SaveProfessorRequest {
    private Long departmentId;
    private String name;
    private Long imageId;
    private String content;
}
