package io.my.college;

import io.my.base.annotation.Logger;
import io.my.base.entity.CollegeEmailRequest;
import io.my.base.payload.BaseExtentionResponse;
import io.my.base.payload.BaseResponse;
import io.my.college.payload.response.CollegeSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/college")
public class CollegeController {
    private final CollegeService collegeService;

    @Logger
    @GetMapping("/list")
    public Mono<BaseExtentionResponse<List<CollegeSearchResponse>>> collegeSearch(
            @RequestParam(value = "search", required = false, defaultValue = "") String search) {
        return collegeService.collegeSearch(search);
    }

    @Logger
    @PostMapping("/email")
    public Mono<BaseResponse> postCollegeEmailRequest(
            @RequestBody CollegeEmailRequest requestBody) {
        return collegeService.postCollegeEmailRequest(requestBody);
    }
}
