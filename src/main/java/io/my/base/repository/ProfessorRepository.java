package io.my.base.repository;

import io.my.base.entity.Professor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProfessorRepository extends ReactiveCrudRepository<Professor, Long> {
}
