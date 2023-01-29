package io.my.base.repository;

import io.my.base.entity.Department;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface DepartmentRepository extends ReactiveCrudRepository<Department, Long> {
    Flux<Department> findByCollegeIdAndNameContaining(Long collegeId, String name);
}
