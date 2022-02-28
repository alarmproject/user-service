package io.my.college.payload.response;

import io.my.base.payload.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollegeSearchResponse {
    private Long id;
    private String name;
}
