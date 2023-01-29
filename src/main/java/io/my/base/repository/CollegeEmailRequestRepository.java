package io.my.base.repository;

import io.my.base.entity.CollegeEmailRequest;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CollegeEmailRequestRepository extends ReactiveCrudRepository<CollegeEmailRequest, Long> {
}
