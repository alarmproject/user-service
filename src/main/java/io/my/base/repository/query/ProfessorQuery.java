package io.my.base.repository.query;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfessorQuery {
    private final DatabaseClient client;

    public DatabaseClient.GenericExecuteSpec findByCollegeIdAndNameContaining(Long collegeId, String name) {
        String query =
                "select " +
                "p.* " +
                ", d.name as department_name " +
                ", i.file_name " +
                "from " +
                "professor p " +
                "join department d on p.department_id = d.id " +
                "left join image i on p.image_id = i.id " +
                "where " +
                "p.college_id = :collegeId " +
                "and " +
                "p.name like CONCAT('%', :name, '%') "
                ;

        return client.sql(query).bind("collegeId", collegeId).bind("name", name);
    }

}
