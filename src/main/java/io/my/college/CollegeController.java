package io.my.college;

import io.my.college.payload.response.CollegeSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/college")
public class CollegeController {
    private final CollegeService collegeService;

    @GetMapping("/list")
    public Mono<CollegeSearchResponse> collegeSearch(
            @RequestParam(value = "search", required = false, defaultValue = "") String search) {
        return collegeService.colleageSearch(search);
    }
}
