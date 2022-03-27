package io.my.base.repository.dao;

import io.my.base.entity.Department;
import io.my.base.entity.Professor;
import io.my.base.repository.query.ProfessorQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class ProfessorDAO {
    private final ProfessorQuery professorQuery;

    public Flux<Professor> findByCollegeIdAndNameContaining(Long collegeId, String name) {
        return this.professorQuery.findByCollegeIdAndNameContaining(collegeId, name).map((row, rowMetadata) ->
                Professor.builder()
                    .id(row.get("id", Long.class))
                    .name(row.get("name", String.class))
                    .department(Department.builder().name(row.get("department_name", String.class)).build())
                    .build())
        .all();

    }


}
