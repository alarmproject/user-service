package io.my.department;

import io.my.base.payload.BaseExtentionPagingResponse;
import io.my.base.payload.BaseExtentionResponse;
import io.my.department.payload.request.SaveDepartmentRequest;
import io.my.department.payload.response.DepartmentListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping
    public Mono<BaseExtentionResponse<Long>> saveDepartment(
            @RequestBody SaveDepartmentRequest requestBody) {
        return departmentService.saveDepartment(requestBody);
    }

    @GetMapping
    public Mono<BaseExtentionResponse<List<DepartmentListResponse>>> findDepartmentList(
            @RequestParam("name") String name) {
        return departmentService.findDepartmentList(name);
    }

}
