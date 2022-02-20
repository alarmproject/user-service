package io.my.base.repository;

import io.my.base.entity.College;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CollegeRepository extends ReactiveCrudRepository<College, Long> {
    Flux<College> findByNameContaining(String name);
}
