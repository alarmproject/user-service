package io.my.college.payload.response;

import io.my.base.payload.BaseResponse;
import io.my.college.payload.CollegeIdAndName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CollegeSearchResponse extends BaseResponse {
    private List<CollegeIdAndName> list;
}
