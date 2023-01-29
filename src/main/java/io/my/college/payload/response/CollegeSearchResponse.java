package io.my.college.payload.response;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollegeSearchResponse {
    private Long id;
    private String name;
    private List<String> emailPrefixList;
}
