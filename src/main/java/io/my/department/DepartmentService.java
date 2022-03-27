package io.my.department;

import io.my.base.context.JwtContextHolder;
import io.my.base.entity.Department;
import io.my.base.payload.BaseExtentionResponse;
import io.my.base.repository.DepartmentRepository;
import io.my.base.repository.UserRepository;
import io.my.department.payload.request.SaveDepartmentRequest;
import io.my.department.payload.response.DepartmentListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public Mono<BaseExtentionResponse<Long>> saveDepartment(SaveDepartmentRequest requestBody) {
        return JwtContextHolder.getMonoUserId()
                .flatMap(userRepository::findById)
                .flatMap(user -> {
                    Department department = Department.builder()
                            .collegeId(user.getCollegeId())
                            .name(requestBody.getName())
                            .build()
                            ;
                    return departmentRepository.save(department);
                })
                .map(entity -> new BaseExtentionResponse(entity.getId()));
    }

    public Mono<BaseExtentionResponse<List<DepartmentListResponse>>> findDepartmentList(String name) {
        return JwtContextHolder.getMonoUserId()
                .flatMap(userRepository::findById)
                .flatMapMany(entity ->
                        departmentRepository.findByCollegeIdAndNameContaining(entity.getCollegeId(), name)
                )
                .collectList()
                .map(list -> {
                    List<DepartmentListResponse> responseList = new ArrayList<>();
                    list.forEach(entity ->
                        responseList.add(
                                DepartmentListResponse.builder()
                                        .id(entity.getId())
                                        .name(entity.getName())
                                        .build()));
                    return responseList;
                })
                .map(BaseExtentionResponse::new);
    }
}
